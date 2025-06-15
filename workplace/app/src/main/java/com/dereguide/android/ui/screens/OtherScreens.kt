package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dereguide.android.R
import com.dereguide.android.ui.components.CharacterItem
import com.dereguide.android.ui.components.SearchBar
import com.dereguide.android.ui.components.SongItem
import com.dereguide.android.ui.viewmodel.CharacterListViewModel
import com.dereguide.android.ui.viewmodel.SongListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    navController: NavController,
    viewModel: SongListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { 
                searchQuery = it
                viewModel.searchSongs(it)
            },
            onFilterClick = { showFilters = !showFilters },
            placeholder = stringResource(R.string.search_songs)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter section
        if (showFilters) {
            SongFilterSection(
                selectedAttribute = uiState.selectedAttribute,
                selectedDifficulty = uiState.selectedDifficulty,
                onAttributeChange = viewModel::filterByAttribute,
                onDifficultyChange = viewModel::filterByDifficulty
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Songs list
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error,
                    onRetry = viewModel::loadSongs
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.songs) { song ->
                        SongItem(
                            song = song,
                            onClick = { 
                                // Navigate to song detail screen
                                navController.navigate("song_detail/${song.id}")
                            },
                            onPlayClick = {
                                // Play song preview
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { 
                searchQuery = it
                viewModel.searchCharacters(it)
            },
            onFilterClick = { showFilters = !showFilters },
            placeholder = stringResource(R.string.search_characters)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter section
        if (showFilters) {
            CharacterFilterSection(
                selectedAttribute = uiState.selectedAttribute,
                onAttributeChange = viewModel::filterByAttribute
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Characters list
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error,
                    onRetry = viewModel::loadCharacters
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.characters) { character ->
                        CharacterItem(
                            character = character,
                            onClick = { 
                                // Navigate to character detail screen
                                navController.navigate("character_detail/${character.id}")
                            },
                            showBirthdayBadge = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SongFilterSection(
    selectedAttribute: String?,
    selectedDifficulty: String?,
    onAttributeChange: (String?) -> Unit,
    onDifficultyChange: (String?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Attribute filter
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedAttribute == null,
                    onClick = { onAttributeChange(null) },
                    label = { Text(stringResource(R.string.all)) }
                )
                FilterChip(
                    selected = selectedAttribute == "cute",
                    onClick = { onAttributeChange("cute") },
                    label = { Text(stringResource(R.string.cute)) }
                )
                FilterChip(
                    selected = selectedAttribute == "cool",
                    onClick = { onAttributeChange("cool") },
                    label = { Text(stringResource(R.string.cool)) }
                )
                FilterChip(
                    selected = selectedAttribute == "passion",
                    onClick = { onAttributeChange("passion") },
                    label = { Text(stringResource(R.string.passion)) }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Difficulty filter
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDifficulty == null,
                    onClick = { onDifficultyChange(null) },
                    label = { Text(stringResource(R.string.all)) }
                )
                listOf("debut", "regular", "pro", "master", "master+").forEach { difficulty ->
                    FilterChip(
                        selected = selectedDifficulty == difficulty,
                        onClick = { onDifficultyChange(difficulty) },
                        label = { Text(difficulty.capitalize()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterFilterSection(
    selectedAttribute: String?,
    onAttributeChange: (String?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Attribute filter
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedAttribute == null,
                    onClick = { onAttributeChange(null) },
                    label = { Text(stringResource(R.string.all)) }
                )
                FilterChip(
                    selected = selectedAttribute == "cute",
                    onClick = { onAttributeChange("cute") },
                    label = { Text(stringResource(R.string.cute)) }
                )
                FilterChip(
                    selected = selectedAttribute == "cool",
                    onClick = { onAttributeChange("cool") },
                    label = { Text(stringResource(R.string.cool)) }
                )
                FilterChip(
                    selected = selectedAttribute == "passion",
                    onClick = { onAttributeChange("passion") },
                    label = { Text(stringResource(R.string.passion)) }
                )
            }
        }
    }
}

@Composable
fun TeamEditorScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_team),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Team editor and live score simulator will be implemented here",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun MoreScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_more),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Additional features like gacha simulator, events, and settings will be here",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}
