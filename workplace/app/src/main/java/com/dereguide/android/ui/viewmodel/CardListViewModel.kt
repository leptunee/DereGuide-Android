package com.dereguide.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CardListUiState())
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedAttribute = MutableStateFlow<String?>(null)
    private val _selectedRarity = MutableStateFlow<Int?>(null)
    
    init {
        loadCards()
        observeFilters()
    }
    
    private fun observeFilters() {
        combine(
            _searchQuery,
            _selectedAttribute,
            _selectedRarity,
            cardRepository.getAllCards()
        ) { query, attribute, rarity, allCards ->
            var filteredCards = allCards
            
            // Apply search filter
            if (query.isNotBlank()) {
                filteredCards = filteredCards.filter { card ->
                    card.name.contains(query, ignoreCase = true)
                }
            }
            
            // Apply attribute filter
            if (attribute != null) {
                filteredCards = filteredCards.filter { card ->
                    card.attribute == attribute
                }
            }
            
            // Apply rarity filter
            if (rarity != null) {
                filteredCards = filteredCards.filter { card ->
                    card.rarity == rarity
                }
            }
            
            filteredCards
        }.onEach { cards ->
            _uiState.update { currentState ->
                currentState.copy(
                    cards = cards,
                    isLoading = false,
                    error = null,
                    selectedAttribute = _selectedAttribute.value,
                    selectedRarity = _selectedRarity.value
                )
            }
        }.launchIn(viewModelScope)
    }
    
    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                cardRepository.refreshCardsIfEmpty()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }
    
    fun searchCards(query: String) {
        _searchQuery.value = query
    }
    
    fun filterByAttribute(attribute: String?) {
        _selectedAttribute.value = attribute
    }
    
    fun filterByRarity(rarity: Int?) {
        _selectedRarity.value = rarity
    }
}

data class CardListUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedAttribute: String? = null,
    val selectedRarity: Int? = null
)
