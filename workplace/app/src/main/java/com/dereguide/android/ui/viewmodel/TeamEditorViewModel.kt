package com.dereguide.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Card
import com.dereguide.android.data.model.Team
import com.dereguide.android.data.repository.CardRepository
import com.dereguide.android.data.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamEditorViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val cardRepository: CardRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TeamEditorUiState())
    val uiState: StateFlow<TeamEditorUiState> = _uiState.asStateFlow()
    
    private val _selectedCards = MutableStateFlow<List<Card>>(emptyList())
    val selectedCards: StateFlow<List<Card>> = _selectedCards.asStateFlow()
    
    private val _guestCard = MutableStateFlow<Card?>(null)
    val guestCard: StateFlow<Card?> = _guestCard.asStateFlow()
    
    init {
        loadTeams()
        loadAvailableCards()
    }
    
    private fun loadTeams() {
        teamRepository.getAllTeams()
            .onEach { teams ->
                _uiState.update { it.copy(teams = teams, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }
    
    private fun loadAvailableCards() {
        cardRepository.getAllCards()
            .onEach { cards ->
                _uiState.update { it.copy(availableCards = cards) }
            }
            .launchIn(viewModelScope)
    }
    
    fun selectCard(card: Card) {
        val currentCards = _selectedCards.value.toMutableList()
        if (currentCards.size < 5 && !currentCards.contains(card)) {
            currentCards.add(card)
            _selectedCards.value = currentCards
            updateTeamStats()
        }
    }
    
    fun removeCard(card: Card) {
        val currentCards = _selectedCards.value.toMutableList()
        currentCards.remove(card)
        _selectedCards.value = currentCards
        updateTeamStats()
    }
    
    fun setGuestCard(card: Card?) {
        _guestCard.value = card
        updateTeamStats()
    }
    
    fun saveTeam(teamName: String) {
        viewModelScope.launch {
            if (_selectedCards.value.isNotEmpty() && teamName.isNotBlank()) {
                try {
                    val cardIds = _selectedCards.value.map { it.id }
                    val guestCardId = _guestCard.value?.id
                    
                    teamRepository.createTeam(teamName, cardIds, guestCardId)
                    
                    // Clear selection after saving
                    clearSelection()
                    
                    _uiState.update { 
                        it.copy(showSaveSuccess = true) 
                    }
                } catch (e: Exception) {
                    _uiState.update { 
                        it.copy(error = e.message ?: "Failed to save team") 
                    }
                }
            }
        }
    }
    
    fun loadTeam(team: Team) {
        viewModelScope.launch {
            val cards = team.cardIds.mapNotNull { cardId ->
                cardRepository.getCardById(cardId)
            }
            _selectedCards.value = cards
            
            team.guestCardId?.let { guestId ->
                _guestCard.value = cardRepository.getCardById(guestId)
            }
            
            updateTeamStats()
        }
    }
    
    fun deleteTeam(team: Team) {
        viewModelScope.launch {
            try {
                teamRepository.deleteTeam(team)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete team") 
                }
            }
        }
    }
    
    fun clearSelection() {
        _selectedCards.value = emptyList()
        _guestCard.value = null
        updateTeamStats()
    }
    
    private fun updateTeamStats() {
        val cards = _selectedCards.value
        val guest = _guestCard.value
        
        val totalVocal = cards.sumOf { it.vocal } + (guest?.vocal ?: 0)
        val totalDance = cards.sumOf { it.dance } + (guest?.dance ?: 0)
        val totalVisual = cards.sumOf { it.visual } + (guest?.visual ?: 0)
        val totalPower = totalVocal + totalDance + totalVisual
        
        _uiState.update { 
            it.copy(
                totalVocal = totalVocal,
                totalDance = totalDance,
                totalVisual = totalVisual,
                totalPower = totalPower
            ) 
        }
    }
    
    fun dismissSaveSuccess() {
        _uiState.update { it.copy(showSaveSuccess = false) }
    }
    
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class TeamEditorUiState(
    val teams: List<Team> = emptyList(),
    val availableCards: List<Card> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val totalVocal: Int = 0,
    val totalDance: Int = 0,
    val totalVisual: Int = 0,
    val totalPower: Int = 0,
    val showSaveSuccess: Boolean = false
)
