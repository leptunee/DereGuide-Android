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
class FavoriteCardsViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "FavoriteCardsViewModel"
    }
    
    private val _uiState = MutableStateFlow(FavoriteCardsUiState())
    val uiState: StateFlow<FavoriteCardsUiState> = _uiState.asStateFlow()
    
    init {
        Log.d(TAG, "FavoriteCardsViewModel initialized")
        loadFavoriteCards()
    }
    
    private fun loadFavoriteCards() {
        Log.d(TAG, "Loading favorite cards...")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            cardRepository.getFavoriteCards()
                .catch { exception ->
                    Log.e(TAG, "Error loading favorite cards", exception)
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = exception.message ?: "Unknown error occurred"
                        ) 
                    }
                }
                .collect { favoriteCards: List<Card> ->
                    Log.d(TAG, "Favorite cards loaded: ${favoriteCards.size} cards")
                    _uiState.update { 
                        it.copy(
                            favoriteCards = favoriteCards,
                            isLoading = false,
                            error = null
                        ) 
                    }
                }
        }
    }
    
    fun toggleFavorite(cardId: Int) {
        viewModelScope.launch {
            try {
                cardRepository.toggleFavorite(cardId)
                Log.d(TAG, "Toggled favorite for card: $cardId")
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite", e)
            }
        }
    }
    
    fun refreshFavoriteCards() {
        loadFavoriteCards()
    }
}

data class FavoriteCardsUiState(
    val favoriteCards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
