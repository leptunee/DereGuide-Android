package com.dereguide.android.data.repository

import android.util.Log
import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.CardDao
import com.dereguide.android.data.model.Card
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
    }
    
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
    
    suspend fun refreshCards(): Result<Unit> {
        return try {
            Log.d(TAG, "开始从 API 获取卡片数据...")
            val response = apiService.getAllCards()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Log.d(TAG, "API 返回 ${apiResponse.result.size} 张卡片")
                    
                    // 转换前100张卡片数据（避免数据量过大）
                    val cards = apiResponse.result.take(100).map { cardListItem ->
                        ApiDataMapper.mapCardListItemToCard(cardListItem)
                    }
                    
                    Log.d(TAG, "转换后准备插入 ${cards.size} 张卡片到数据库")
                    cardDao.insertCards(cards)
                    Log.d(TAG, "卡片数据插入完成")
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
        cardDao.insertCards(sampleCards)
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
    }    // 新增收藏功能
    fun getFavoriteCards(): Flow<List<Card>> = cardDao.getFavoriteCards()
    
    suspend fun toggleFavorite(cardId: Int) {
        val card = cardDao.getCardById(cardId)
        card?.let {
            cardDao.updateFavoriteStatus(cardId, !it.isFavorite)
        }
    }
    
    // 新增排序功能
    suspend fun getCardsSortedByStats(): List<Card> = cardDao.getCardsByTotalStats()
    
    suspend fun getCardsBySkillType(skillType: String): List<Card> = cardDao.getCardsBySkillType(skillType)
    
    suspend fun getCardsByRarityAndAttribute(rarity: Int, attribute: String): List<Card> = 
        cardDao.getCardsByRarityAndAttribute(rarity, attribute)

    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }
      suspend fun updateCard(card: Card) {
        cardDao.updateCard(card)
    }

    suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(card)
    }
}
