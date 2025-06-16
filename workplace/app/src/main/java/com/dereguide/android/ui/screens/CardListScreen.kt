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
import com.dereguide.android.ui.components.CardItem
import com.dereguide.android.ui.components.SearchBar
import com.dereguide.android.ui.viewmodel.CardListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    navController: NavController,
    viewModel: CardListViewModel = hiltViewModel()
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
                viewModel.searchCards(it)
            },
            onFilterClick = { showFilters = !showFilters },
            placeholder = stringResource(R.string.search_cards)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter section
        if (showFilters) {
            FilterSection(
                selectedAttribute = uiState.selectedAttribute,
                selectedRarity = uiState.selectedRarity,
                onAttributeChange = viewModel::filterByAttribute,
                onRarityChange = viewModel::filterByRarity
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Cards list
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }            uiState.error != null -> {
                val errorMessage = uiState.error
                if (errorMessage != null) {
                    ErrorMessage(
                        message = errorMessage,
                        onRetry = viewModel::loadCards
                    )
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.cards) { card ->
                        CardItem(
                            card = card,
                            onClick = { 
                                // Navigate to card detail screen
                                navController.navigate("card_detail/${card.id}")
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
private fun FilterSection(
    selectedAttribute: String?,
    selectedRarity: Int?,
    onAttributeChange: (String?) -> Unit,
    onRarityChange: (Int?) -> Unit
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
            
            // Rarity filter
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedRarity == null,
                    onClick = { onRarityChange(null) },
                    label = { Text(stringResource(R.string.all)) }
                )
                for (rarity in 1..3) {
                    FilterChip(
                        selected = selectedRarity == rarity,
                        onClick = { onRarityChange(rarity) },
                        label = { Text("${"★".repeat(rarity)}") }
                    )
                }
            }
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
