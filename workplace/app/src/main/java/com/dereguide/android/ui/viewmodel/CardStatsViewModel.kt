package com.dereguide.android.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardStatsViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "CardStatsViewModel"
    }
    
    private val _uiState = MutableStateFlow(CardStatsUiState())
    val uiState: StateFlow<CardStatsUiState> = _uiState.asStateFlow()
    
    init {
        Log.d(TAG, "CardStatsViewModel initialized")
        loadStats()
    }
    
    fun loadStats() {
        Log.d(TAG, "Loading card statistics...")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {                combine(
                    cardRepository.getAllCards(),
                    cardRepository.getFavoriteCards()
                ) { allCards: List<Card>, favoriteCards: List<Card> ->
                    val totalCards = allCards.size
                    val favoriteCount = favoriteCards.size
                    
                    // 按属性统计
                    val cuteCards = allCards.count { it.attribute == "cute" }
                    val coolCards = allCards.count { it.attribute == "cool" }
                    val passionCards = allCards.count { it.attribute == "passion" }
                    
                    // 按稀有度统计
                    val normalCards = allCards.count { it.rarity == 1 }
                    val rareCards = allCards.count { it.rarity == 2 }
                    val superRareCards = allCards.count { it.rarity == 3 }
                      // 最高属性值统计
                    val maxVocal = allCards.maxOfOrNull { card -> card.vocal2 ?: card.vocal } ?: 0
                    val maxDance = allCards.maxOfOrNull { card -> card.dance2 ?: card.dance } ?: 0
                    val maxVisual = allCards.maxOfOrNull { card -> card.visual2 ?: card.visual } ?: 0
                    val maxTotalStats = allCards.maxOfOrNull { card -> card.maxTotalStats } ?: 0
                    
                    CardStatsUiState(
                        totalCards = totalCards,
                        favoriteCards = favoriteCount,
                        cuteCards = cuteCards,
                        coolCards = coolCards,
                        passionCards = passionCards,
                        normalCards = normalCards,
                        rareCards = rareCards,
                        superRareCards = superRareCards,
                        maxVocal = maxVocal,
                        maxDance = maxDance,
                        maxVisual = maxVisual,
                        maxTotalStats = maxTotalStats,
                        isLoading = false,
                        error = null
                    )
                }.catch { exception ->
                    Log.e(TAG, "Error loading card stats", exception)
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = exception.message ?: "Unknown error occurred"
                        ) 
                    }
                }.collect { stats ->
                    Log.d(TAG, "Card stats loaded: ${stats.totalCards} total cards")
                    _uiState.value = stats
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error in loadStats", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }
}

data class CardStatsUiState(
    val totalCards: Int = 0,
    val favoriteCards: Int = 0,
    val cuteCards: Int = 0,
    val coolCards: Int = 0,
    val passionCards: Int = 0,
    val normalCards: Int = 0,
    val rareCards: Int = 0,
    val superRareCards: Int = 0,
    val maxVocal: Int = 0,
    val maxDance: Int = 0,
    val maxVisual: Int = 0,
    val maxTotalStats: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
