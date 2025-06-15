package com.dereguide.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dereguide.android.data.model.Song
import com.dereguide.android.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val songRepository: SongRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SongListUiState())
    val uiState: StateFlow<SongListUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val _selectedAttribute = MutableStateFlow<String?>(null)
    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    
    init {
        loadSongs()
        observeFilters()
    }
    
    private fun observeFilters() {
        combine(
            _searchQuery,
            _selectedAttribute,
            _selectedDifficulty,
            songRepository.getAllSongs()
        ) { query, attribute, difficulty, allSongs ->
            var filteredSongs = allSongs
            
            // Apply search filter
            if (query.isNotBlank()) {
                filteredSongs = filteredSongs.filter { song ->
                    song.name.contains(query, ignoreCase = true) ||
                    song.artist?.contains(query, ignoreCase = true) == true
                }
            }
            
            // Apply attribute filter
            if (attribute != null) {
                filteredSongs = filteredSongs.filter { song ->
                    song.attribute == attribute
                }
            }
            
            // Apply difficulty filter
            if (difficulty != null) {
                filteredSongs = filteredSongs.filter { song ->
                    song.difficulty?.containsKey(difficulty) == true
                }
            }
            
            filteredSongs.sortedBy { it.name }
        }.onEach { songs ->
            _uiState.update { currentState ->
                currentState.copy(
                    songs = songs,
                    isLoading = false,
                    error = null,
                    selectedAttribute = _selectedAttribute.value,
                    selectedDifficulty = _selectedDifficulty.value
                )
            }
        }.launchIn(viewModelScope)
    }
    
    fun loadSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                songRepository.refreshSongsIfEmpty()
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
    
    fun searchSongs(query: String) {
        _searchQuery.value = query
    }
    
    fun filterByAttribute(attribute: String?) {
        _selectedAttribute.value = attribute
    }
    
    fun filterByDifficulty(difficulty: String?) {
        _selectedDifficulty.value = difficulty
    }
}

data class SongListUiState(
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedAttribute: String? = null,
    val selectedDifficulty: String? = null
)
