package com.dereguide.android.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.repository.CardRepository
import com.dereguide.android.ui.components.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "CardListViewModel"
    }
    
    private val _uiState = MutableStateFlow(CardListUiState())
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedAttribute = MutableStateFlow<String?>(null)
    private val _selectedRarity = MutableStateFlow<Int?>(null)
    private val _sortOrder = MutableStateFlow(SortOrder.DEFAULT)
    private val _showFavoritesOnly = MutableStateFlow(false)
        init {
        Log.d(TAG, "CardListViewModel initialized")
        // 改进的加载策略：优先显示本地数据，后台异步刷新
        loadCardsOptimized()
        observeFilters()
    }
      private fun observeFilters() {
        combine(
            _searchQuery,
            _selectedAttribute,
            _selectedRarity,
            _sortOrder,
            _showFavoritesOnly
        ) { query, attribute, rarity, sortOrder, favoritesOnly ->
            CardFilterParams(query, attribute, rarity, sortOrder, favoritesOnly)
        }.flatMapLatest { params ->
            val cardsFlow = if (params.favoritesOnly) {
                cardRepository.getFavoriteCards()
            } else {
                cardRepository.getAllCards()
            }
            
            cardsFlow.map { allCards ->
                var filteredCards = allCards
                
                // Apply search filter
                if (params.query.isNotBlank()) {
                    filteredCards = filteredCards.filter { card ->
                        card.name.contains(params.query, ignoreCase = true)
                    }
                }
                
                // Apply attribute filter
                params.attribute?.let { attr ->
                    filteredCards = filteredCards.filter { card ->
                        card.attribute == attr
                    }
                }
                
                // Apply rarity filter
                params.rarity?.let { rar ->
                    filteredCards = filteredCards.filter { card ->
                        card.rarity == rar
                    }
                }
                
                // Apply sorting
                filteredCards = when (params.sortOrder) {
                    SortOrder.DEFAULT -> filteredCards.sortedByDescending { it.id }
                    SortOrder.TOTAL_STATS_DESC -> filteredCards.sortedByDescending { it.maxTotalStats }
                    SortOrder.TOTAL_STATS_ASC -> filteredCards.sortedBy { it.maxTotalStats }
                    SortOrder.VOCAL_DESC -> filteredCards.sortedByDescending { it.vocal2 ?: it.vocal }
                    SortOrder.DANCE_DESC -> filteredCards.sortedByDescending { it.dance2 ?: it.dance }
                    SortOrder.VISUAL_DESC -> filteredCards.sortedByDescending { it.visual2 ?: it.visual }
                    SortOrder.RARITY_DESC -> filteredCards.sortedByDescending { it.rarity }
                    SortOrder.RARITY_ASC -> filteredCards.sortedBy { it.rarity }
                    SortOrder.NAME_ASC -> filteredCards.sortedBy { it.name }
                    SortOrder.RELEASE_DATE_DESC -> filteredCards.sortedByDescending { it.releaseDate ?: "" }
                }
                
                FilteredCardsResult(filteredCards, params)
            }
        }.onEach { result ->
            Log.d(TAG, "Cards filtered: ${result.cards.size} cards")
            _uiState.update { currentState ->
                currentState.copy(
                    cards = result.cards,
                    isLoading = false,
                    error = null,
                    selectedAttribute = result.params.attribute,
                    selectedRarity = result.params.rarity,
                    sortOrder = result.params.sortOrder,
                    showFavoritesOnly = result.params.favoritesOnly
                )
            }
        }.catch { exception ->
            Log.e(TAG, "Error in observeFilters", exception)
            _uiState.update { 
                it.copy(
                    isLoading = false, 
                    error = exception.message ?: "Unknown error occurred"
                ) 
            }        }.launchIn(viewModelScope)
    }

    fun loadCards() {
        Log.d(TAG, "Loading cards...")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Force refresh to get API data
                cardRepository.forceRefreshCards()
                Log.d(TAG, "Cards loaded successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading cards", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }

    /**
     * 优化的卡片加载策略：
     * 1. 立即显示本地缓存数据（如果有）
     * 2. 后台异步检查是否需要刷新
     * 3. 如果本地没有数据，则加载示例数据保证快速响应
     */
    private fun loadCardsOptimized() {
        Log.d(TAG, "Loading cards with optimized strategy...")
        viewModelScope.launch {
            try {
                // 先设置加载状态
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                // 优先使用智能刷新策略，避免每次都强制清空数据库
                cardRepository.smartRefreshCards()
                
                // 加载完成，移除加载状态
                _uiState.update { it.copy(isLoading = false) }
                Log.d(TAG, "Cards loaded successfully with optimized strategy")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading cards", e)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
            }
        }
    }

    fun refreshCards() {
        Log.d(TAG, "Refreshing cards...")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                cardRepository.forceRefreshCards()
                Log.d(TAG, "Cards refreshed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error refreshing cards", e)
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
    
    fun setSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }
    
    fun setShowFavoritesOnly(showFavoritesOnly: Boolean) {
        _showFavoritesOnly.value = showFavoritesOnly
    }
    
    fun clearFilters() {
        _selectedAttribute.value = null
        _selectedRarity.value = null
        _sortOrder.value = SortOrder.DEFAULT
        _showFavoritesOnly.value = false
        _searchQuery.value = ""
    }
    
    fun toggleFavorite(cardId: Int) {
        viewModelScope.launch {
            try {
                cardRepository.toggleFavorite(cardId)
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite", e)
            }
        }
    }
}

data class CardListUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedAttribute: String? = null,
    val selectedRarity: Int? = null,
    val sortOrder: SortOrder = SortOrder.DEFAULT,
    val showFavoritesOnly: Boolean = false
)

private data class CardFilterParams(
    val query: String,
    val attribute: String?,
    val rarity: Int?,
    val sortOrder: SortOrder,
    val favoritesOnly: Boolean
)

private data class FilteredCardsResult(
    val cards: List<Card>,
    val params: CardFilterParams
)
