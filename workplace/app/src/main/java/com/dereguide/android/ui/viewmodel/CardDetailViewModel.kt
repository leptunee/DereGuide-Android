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
class CardDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "CardDetailViewModel"
    }
    
    private val _uiState = MutableStateFlow(CardDetailUiState())
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()
    
    fun loadCard(cardId: Int) {
        Log.d(TAG, "Loading card with ID: $cardId")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val card = cardRepository.getCardById(cardId)
                if (card != null) {
                    _uiState.update { 
                        it.copy(
                            card = card,
                            isLoading = false,
                            error = null
                        ) 
                    }
                    Log.d(TAG, "Card loaded successfully: ${card.name}")
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = "未找到卡片"
                        ) 
                    }
                    Log.w(TAG, "Card not found with ID: $cardId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading card", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "加载卡片时发生未知错误"
                    ) 
                }
            }
        }
    }
}

data class CardDetailUiState(
    val card: Card? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
