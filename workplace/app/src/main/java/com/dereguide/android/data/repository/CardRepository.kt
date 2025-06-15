package com.dereguide.android.data.repository

import com.dereguide.android.data.api.DereGuideApiService
import com.dereguide.android.data.database.CardDao
import com.dereguide.android.data.model.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val apiService: DereGuideApiService,
    private val cardDao: CardDao
) {
    
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
            val response = apiService.getAllCards()
            if (response.isSuccessful) {
                response.body()?.let { cards ->
                    cardDao.insertCards(cards)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch cards: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshCardsIfEmpty() {
        val currentCards = getAllCards().first()
        if (currentCards.isEmpty()) {
            refreshCards()
        }
    }
    
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
