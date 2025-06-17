package com.dereguide.android.data.repository

import android.util.Log
import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.CardDao
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.model.CardListViewItem
import com.dereguide.android.data.model.CardStatistics
import com.dereguide.android.data.SampleDataProvider
import com.dereguide.android.data.mapper.ApiDataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val cardDao: CardDao
) {
    
    companion object {
        private const val TAG = "CardRepository"
        private const val BATCH_SIZE = 500 // 批量插入大小
        private const val DEFAULT_PAGE_SIZE = 20 // 默认分页大小
    }
    
    // 基础查询功能
    fun getAllCards(): Flow<List<Card>> = cardDao.getAllCards()
    
    suspend fun getCardById(cardId: Int): Card? {
        return cardDao.getCardById(cardId)
    }
    
    suspend fun getCardsByCharacter(characterId: Int): List<Card> {
        return cardDao.getCardsByCharacter(characterId)
    }
    
    suspend fun getCardsByAttribute(attribute: String): List<Card> {
        return cardDao.getCardsByAttribute(attribute)
    }
    
    suspend fun getCardsByRarity(rarity: Int): List<Card> {
        return cardDao.getCardsByRarity(rarity)
    }
    
    suspend fun searchCards(query: String): List<Card> {
        return cardDao.searchCards(query)
    }

    // API数据刷新功能
    suspend fun refreshCards(): Result<Unit> {
        return try {
            Log.d(TAG, "开始从 API 获取卡片数据...")
            val response = apiService.getAllCards()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Log.d(TAG, "API 返回 ${apiResponse.result.size} 张卡片")
                    
                    // 处理所有卡片数据（已移除100张限制）
                    val cards = apiResponse.result.map { cardListItem ->
                        ApiDataMapper.mapCardListItemToCard(cardListItem)
                    }
                    
                    Log.d(TAG, "转换后准备插入 ${cards.size} 张卡片到数据库")
                    
                    // 分批插入避免内存压力
                    cards.chunked(BATCH_SIZE).forEach { batch ->
                        cardDao.insertCards(batch)
                        Log.d(TAG, "已插入 ${batch.size} 张卡片")
                    }
                    
                    Log.d(TAG, "卡片数据插入完成，总计 ${cards.size} 张")
                }
                Result.success(Unit)
            } else {
                Log.e(TAG, "API 调用失败: ${response.code()}")
                Result.failure(Exception("Failed to fetch cards: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "刷新卡片数据时出错", e)
            Result.failure(e)
        }
    }
    
    suspend fun refreshCardsIfEmpty() {
        val currentCards = getAllCards().first()
        if (currentCards.isEmpty()) {
            Log.d(TAG, "数据库为空，开始获取数据...")
            // 首先尝试从 API 获取
            val apiResult = refreshCards()
            if (apiResult.isFailure) {
                Log.w(TAG, "API 获取失败，加载示例数据")
                // 如果 API 失败，加载示例数据
                loadSampleData()
            } else {
                Log.d(TAG, "API 数据获取成功")
            }
        } else {
            Log.d(TAG, "数据库已有 ${currentCards.size} 张卡片，跳过刷新")
        }
    }
      private suspend fun loadSampleData() {
        Log.d(TAG, "加载示例数据...")
        val sampleCards = SampleDataProvider.getSampleCards()
        
        // 分批插入示例数据
        sampleCards.chunked(BATCH_SIZE).forEachIndexed { index, batch ->
            cardDao.insertCards(batch)
            Log.d(TAG, "已插入示例数据 ${batch.size} 张卡片 (批次 ${index + 1})")
        }
        
        Log.d(TAG, "示例数据加载完成: ${sampleCards.size} 张卡片")
    }
    
    suspend fun forceRefreshCards(): Result<Unit> {
        return try {
            Log.d(TAG, "强制刷新：清除数据库并重新获取API数据...")
            // 先清空数据库
            cardDao.deleteAllCards()
            Log.d(TAG, "数据库已清空")
            
            // 重新获取API数据
            val result = refreshCards()
            if (result.isFailure) {
                Log.w(TAG, "API 获取失败，加载示例数据作为备用")
                loadSampleData()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "强制刷新时出错", e)
            Result.failure(e)
        }
    }

    // 收藏功能
    fun getFavoriteCards(): Flow<List<Card>> = cardDao.getFavoriteCards()
    
    suspend fun toggleFavorite(cardId: Int) {
        val card = cardDao.getCardById(cardId)
        card?.let {
            cardDao.updateFavoriteStatus(cardId, !it.isFavorite)
        }
    }
    
    // 高级排序和过滤功能
    suspend fun getCardsSortedByStats(): List<Card> = cardDao.getCardsByTotalStats()
    
    suspend fun getCardsBySkillType(skillType: String): List<Card> = cardDao.getCardsBySkillType(skillType)
    
    suspend fun getCardsByRarityAndAttribute(rarity: Int, attribute: String): List<Card> = 
        cardDao.getCardsByRarityAndAttribute(rarity, attribute)

    // 基本CRUD操作
    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }
    
    suspend fun updateCard(card: Card) {
        cardDao.updateCard(card)
    }

    suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(card)
    }
    
    // 分页查询功能（性能优化）
    suspend fun getCardsPaged(offset: Int, limit: Int = DEFAULT_PAGE_SIZE): List<Card> {
        return cardDao.getCardsPaged(offset, limit)
    }
    
    suspend fun getCardsPagedByAttribute(attribute: String, offset: Int, limit: Int = DEFAULT_PAGE_SIZE): List<Card> {
        return cardDao.getCardsPagedByAttribute(attribute, offset, limit)
    }
    
    suspend fun getCardsPagedByRarity(rarity: Int, offset: Int, limit: Int = DEFAULT_PAGE_SIZE): List<Card> {
        return cardDao.getCardsPagedByRarity(rarity, offset, limit)
    }
    
    suspend fun searchCardsPaged(query: String, offset: Int, limit: Int = DEFAULT_PAGE_SIZE): List<Card> {
        return cardDao.searchCardsPaged(query, offset, limit)
    }
    
    suspend fun getCardsCount(): Int {
        return cardDao.getCardsCount()
    }
      // 轻量级查询优化（仅获取列表显示需要的字段）
    suspend fun getCardListItems(): List<CardListViewItem> {
        return cardDao.getCardListItems()
    }
    
    suspend fun getCardListItemsPaged(offset: Int, limit: Int = DEFAULT_PAGE_SIZE): List<CardListViewItem> {
        return cardDao.getCardListItemsPaged(offset, limit)
    }
    
    // 缓存优化：获取最近更新的卡片
    suspend fun getRecentlyUpdatedCards(limit: Int = 50): List<Card> {
        return cardDao.getRecentlyUpdatedCards(limit)
    }
    
    // 性能统计功能
    suspend fun getCardsStatistics(): CardStatistics {
        val totalCount = cardDao.getCardsCount()
        val favoriteCount = cardDao.getFavoriteCards().first().size
        val attributeCounts = mapOf(
            "cute" to cardDao.getCardsByAttribute("cute").size,
            "cool" to cardDao.getCardsByAttribute("cool").size,
            "passion" to cardDao.getCardsByAttribute("passion").size
        )
        val rarityCounts = mapOf(
            2 to cardDao.getCardsByRarity(2).size,
            3 to cardDao.getCardsByRarity(3).size,
            4 to cardDao.getCardsByRarity(4).size,
            5 to cardDao.getCardsByRarity(5).size
        )
          return CardStatistics(
            totalCount = totalCount,
            favoriteCount = favoriteCount,
            attributeCounts = attributeCounts,
            rarityCounts = rarityCounts
        )
    }
}
