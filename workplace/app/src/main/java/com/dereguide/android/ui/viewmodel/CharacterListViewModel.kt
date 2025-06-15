package com.dereguide.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Character
import com.dereguide.android.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedAttribute = MutableStateFlow<String?>(null)
    
    init {
        loadCharacters()
        observeFilters()
    }
    
    private fun observeFilters() {
        combine(
            _searchQuery,
            _selectedAttribute,
            characterRepository.getAllCharacters()
        ) { query, attribute, allCharacters ->
            var filteredCharacters = allCharacters
            
            // Apply search filter
            if (query.isNotBlank()) {
                filteredCharacters = filteredCharacters.filter { character ->
                    character.name.contains(query, ignoreCase = true) ||
                    character.nameKana?.contains(query, ignoreCase = true) == true
                }
            }
            
            // Apply attribute filter
            if (attribute != null) {
                filteredCharacters = filteredCharacters.filter { character ->
                    character.attribute == attribute
                }
            }
            
            filteredCharacters.sortedBy { it.name }
        }.onEach { characters ->
            _uiState.update { currentState ->
                currentState.copy(
                    characters = characters,
                    isLoading = false,
                    error = null,
                    selectedAttribute = _selectedAttribute.value
                )
            }
        }.launchIn(viewModelScope)
    }
    
    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                characterRepository.refreshCharactersIfEmpty()
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
    
    fun searchCharacters(query: String) {
        _searchQuery.value = query
    }
    
    fun filterByAttribute(attribute: String?) {
        _selectedAttribute.value = attribute
    }
    
    fun getTodaysBirthdayCharacters(): Flow<List<Character>> {
        val today = java.time.LocalDate.now()
        val monthDay = String.format("%02d-%02d", today.monthValue, today.dayOfMonth)
        
        return characterRepository.getAllCharacters().map { characters ->
            characters.filter { character ->
                character.birthday?.endsWith(monthDay) == true
            }
        }
    }
}

data class CharacterListUiState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedAttribute: String? = null
)
