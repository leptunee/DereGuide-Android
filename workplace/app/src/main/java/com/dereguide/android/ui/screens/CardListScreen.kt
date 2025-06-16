package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dereguide.android.R
import com.dereguide.android.ui.components.CardFilterSheet
import com.dereguide.android.ui.components.CardGrid
import com.dereguide.android.ui.components.CardItem
import com.dereguide.android.ui.components.SearchBar
import com.dereguide.android.ui.components.SortOrder
import com.dereguide.android.ui.viewmodel.CardListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    navController: NavController,
    viewModel: CardListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var isGridView by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {        // Top bar with search and controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { 
                    searchQuery = it
                    viewModel.searchCards(it)
                },
                onFilterClick = { showFilterSheet = true },
                placeholder = stringResource(R.string.search_cards),
                modifier = Modifier.weight(1f)
            )
            
            // View toggle button
            IconButton(
                onClick = { isGridView = !isGridView }
            ) {
                Icon(
                    imageVector = if (isGridView) Icons.Default.List else Icons.Default.GridView,
                    contentDescription = if (isGridView) "列表视图" else "网格视图"
                )
            }
            
            // Filter button
            IconButton(
                onClick = { showFilterSheet = true }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "筛选"
                )
            }
            
            // Refresh button
            IconButton(
                onClick = { viewModel.refreshCards() },
                enabled = !uiState.isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "刷新卡片数据"
                )
            }
        }        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Active filters indicator
        if (uiState.selectedAttribute != null || uiState.selectedRarity != null || uiState.showFavoritesOnly) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "活跃筛选:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    if (uiState.showFavoritesOnly) {
                        AssistChip(
                            onClick = { },
                            label = { Text("收藏") }
                        )
                    }
                    
                    uiState.selectedAttribute?.let { attr ->
                        AssistChip(
                            onClick = { },
                            label = { Text(attr) }
                        )
                    }
                    
                    uiState.selectedRarity?.let { rarity ->
                        AssistChip(
                            onClick = { },
                            label = { Text("★".repeat(rarity)) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
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
            }            else -> {
                if (isGridView) {
                    CardGrid(
                        cards = uiState.cards,
                        onCardClick = { card ->
                            navController.navigate("card_detail/${card.id}")
                        },
                        onFavoriteClick = { cardId ->
                            viewModel.toggleFavorite(cardId)
                        }
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.cards) { card ->
                            CardItem(
                                card = card,
                                onClick = { 
                                    // Navigate to card detail screen
                                    navController.navigate("card_detail/${card.id}")
                                },
                                onFavoriteClick = {
                                    viewModel.toggleFavorite(card.id)
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Filter sheet
        CardFilterSheet(
            isVisible = showFilterSheet,
            onDismiss = { showFilterSheet = false },
            selectedAttribute = uiState.selectedAttribute,
            selectedRarity = uiState.selectedRarity,
            selectedSortOrder = uiState.sortOrder,
            showFavoritesOnly = uiState.showFavoritesOnly,
            onAttributeChange = viewModel::filterByAttribute,
            onRarityChange = viewModel::filterByRarity,            onSortOrderChange = viewModel::setSortOrder,
            onFavoritesOnlyChange = viewModel::setShowFavoritesOnly,
            onClearFilters = viewModel::clearFilters
        )
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
            Text("重试")
        }
    }
}
