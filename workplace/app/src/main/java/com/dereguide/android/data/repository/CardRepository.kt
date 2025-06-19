package com.dereguide.android.data.repository

import android.util.Log
import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.CardDao
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.model.CardStatistics
import com.dereguide.android.data.SampleDataProvider
import com.dereguide.android.data.mapper.ApiDataMapper
import com.dereguide.android.data.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val cardDao: CardDao,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val TAG = "CardRepository"
        private const val BATCH_SIZE = 50
    }
    
    fun getAllCards(): Flow<List<Card>> = cardDao.getAllCards()
    
    suspend fun getCardById(cardId: Int): Card? = cardDao.getCardById(cardId)
    
    suspend fun getCardsByCharacter(characterId: Int): List<Card> = cardDao.getCardsByCharacter(characterId)
    
    suspend fun getCardsByAttribute(attribute: String): List<Card> = cardDao.getCardsByAttribute(attribute)
    
    suspend fun getCardsByRarity(rarity: Int): List<Card> = cardDao.getCardsByRarity(rarity)
    
    suspend fun searchCards(query: String): List<Card> = cardDao.searchCards(query)

    suspend fun smartRefreshCards(): Flow<List<Card>> = flow {
        val currentTime = System.currentTimeMillis()
        val lastRefreshTime = preferencesManager.getLastCardRefreshTime()
        val cacheAge = currentTime - lastRefreshTime
        
        val isCacheExpired = cacheAge > 24 * 60 * 60 * 1000
        
        val localCards = cardDao.getAllCards().first()
        
        if (localCards.isNotEmpty() && !isCacheExpired) {
            emit(localCards)
        } else if (localCards.isEmpty()) {
            loadInitialSampleData()
            val quickCards = cardDao.getAllCards().first()
            if (quickCards.isNotEmpty()) {
                emit(quickCards)
            }
        } else {
            emit(localCards)
        }
    }

    suspend fun refreshCards(): Result<Unit> {
        return try {
            Log.d(TAG, "开始从 API 获取卡片数据...")
            val response = apiService.getAllCards()
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    Log.d(TAG, "API 返回 ${apiResponse.result.size} 张卡片")
                    
                    val cards = apiResponse.result.map { cardListItem ->
                        val basicCard = ApiDataMapper.mapCardListItemToCard(cardListItem)
                        
                        // 为 SSR 和 SR 卡片获取详细技能信息
                        if (basicCard.rarity >= 3) {
                            try {
                                val detailResponse = apiService.getCard(basicCard.id)
                                if (detailResponse.isSuccessful) {
                                    detailResponse.body()?.result?.let { cardDetail ->
                                        ApiDataMapper.mapCardDetailToCard(cardDetail)
                                    } ?: basicCard
                                } else {
                                    Log.w(TAG, "无法获取卡片 ${basicCard.id} 的详细信息")
                                    basicCard
                                }
                            } catch (e: Exception) {
                                Log.w(TAG, "获取卡片 ${basicCard.id} 详细信息时出错", e)
                                basicCard
                            }
                        } else {
                            basicCard
                        }
                    }
                    
                    cards.chunked(BATCH_SIZE).forEach { batch ->
                        cardDao.insertCards(batch)
                    }
                    
                    preferencesManager.setLastCardRefreshTime(System.currentTimeMillis())
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

    private suspend fun loadInitialSampleData() {
        Log.d(TAG, "开始加载初始示例数据...")
        
        val basicCards = SampleDataProvider.generateSampleCards(50)
        
        basicCards.chunked(25).forEachIndexed { _, batch ->
            cardDao.insertCards(batch)
            Log.d(TAG, "已插入初始示例数据  张卡片 (批次 )")
        }
        
        Log.d(TAG, "初始示例数据加载完成:  张卡片")
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "后台加载更多示例数据...")
                val allCards = SampleDataProvider.getSampleCards()
                val additionalCards = allCards.drop(50)
                
                additionalCards.chunked(BATCH_SIZE).forEach { batch ->
                    cardDao.insertCards(batch)
                }
                Log.d(TAG, "后台示例数据加载完成:  张卡片")
            } catch (e: Exception) {
                Log.w(TAG, "后台加载更多数据失败", e)
            }
        }
    }

    private suspend fun loadSampleData() {
        Log.d(TAG, "加载示例数据...")
        val sampleCards = SampleDataProvider.getSampleCards()
        
        cardDao.deleteAllCards()
        
        sampleCards.chunked(BATCH_SIZE).forEachIndexed { _, batch ->
            cardDao.insertCards(batch)
            Log.d(TAG, "已插入示例数据  张卡片 (批次 )")
        }
        
        Log.d(TAG, "示例数据加载完成:  张卡片")
    }

    suspend fun forceRefreshCards(): Result<Unit> {
        return try {
            Log.d(TAG, "开始强制刷新卡片数据...")
            
            cardDao.deleteAllCards()
            
            val apiResult = refreshCards()
            if (apiResult.isFailure) {
                Log.w(TAG, "API 获取失败，使用示例数据")
                loadSampleData()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "强制刷新失败", e)
            Result.failure(e)
        }
    }

    suspend fun refreshCardsIfEmpty() {
        val currentCards = getAllCards().first()
        if (currentCards.isEmpty()) {
            Log.d(TAG, "数据库为空，开始获取数据...")
            val apiResult = refreshCards()
            if (apiResult.isFailure) {
                Log.w(TAG, "API 获取失败，加载示例数据")
                loadSampleData()
            } else {
                Log.d(TAG, "API 数据获取成功")
            }
        } else {
            Log.d(TAG, "数据库已有  张卡片，跳过刷新")
        }
    }

    suspend fun getCardStatistics(): CardStatistics {
        val allCards = getAllCards().first()
        val favoriteCards = getFavoriteCards().first()
        return CardStatistics(
            totalCount = allCards.size,
            favoriteCount = favoriteCards.size,
            attributeCounts = allCards.groupBy { it.attribute }.mapValues { it.value.size },
            rarityCounts = allCards.groupBy { it.rarity }.mapValues { it.value.size }
        )
    }

    // 收藏相关方法
    fun getFavoriteCards(): Flow<List<Card>> = cardDao.getFavoriteCards()

    suspend fun toggleFavorite(cardId: Int) {
        cardDao.toggleFavorite(cardId)
    }

    suspend fun addToFavorites(cardId: Int) {
        cardDao.addToFavorites(cardId)
    }

    suspend fun removeFromFavorites(cardId: Int) {
        cardDao.removeFromFavorites(cardId)
    }

    /**
     * 从 API 获取卡片详细信息（包含技能数据）
     */
    suspend fun getCardDetailFromApi(cardId: Int): Card? {
        return try {
            Log.d(TAG, "Fetching card detail from API for ID: $cardId")
            val response = apiService.getCard(cardId)
            if (response.isSuccessful) {
                response.body()?.result?.let { cardDetail ->
                    ApiDataMapper.mapCardDetailToCard(cardDetail)
                }
            } else {
                Log.e(TAG, "API call failed for card detail: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching card detail from API", e)
            null
        }
    }
    
    /**
     * 更新卡片信息到数据库
     */
    suspend fun updateCard(card: Card) {
        cardDao.updateCard(card)
    }

    /**
     * 强制重置数据库并加载新的示例数据（用于测试）
     */
    suspend fun resetWithSampleData(): Result<Unit> {
        return try {
            Log.d(TAG, "强制重置数据库并加载示例数据...")
            
            // 清空数据库
            cardDao.deleteAllCards()
            
            // 加载示例数据
            val sampleCards = SampleDataProvider.getSampleCards()
            
            sampleCards.chunked(BATCH_SIZE).forEachIndexed { index, batch ->
                cardDao.insertCards(batch)
                Log.d(TAG, "已插入示例数据批次 ${index + 1}: ${batch.size} 张卡片")
            }
            
            // 更新刷新时间
            preferencesManager.setLastCardRefreshTime(System.currentTimeMillis())
            
            Log.d(TAG, "示例数据重置完成，总计 ${sampleCards.size} 张卡片")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "重置示例数据时出错", e)
            Result.failure(e)
        }
    }

    suspend fun isFavorite(cardId: Int): Boolean {
        return cardDao.isFavorite(cardId)
    }
}
