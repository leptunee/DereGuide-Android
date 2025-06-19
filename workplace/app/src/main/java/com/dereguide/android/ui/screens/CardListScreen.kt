package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
) {    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }
    var isGridView by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header with title and refresh button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.card_library),
                style = MaterialTheme.typography.headlineMedium
            )
            
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
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { 
                searchQuery = it
                viewModel.searchCards(it)
            },
            onFilterClick = { showFilterSheet = true },
            placeholder = stringResource(R.string.search_cards),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
          // Active filters indicator
        if (uiState.selectedAttribute != null || uiState.selectedRarityTier != null || uiState.showFavoritesOnly) {
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
                        text = stringResource(R.string.active_filters),
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
                    
                    uiState.selectedRarityTier?.let { rarityTier ->
                        AssistChip(
                            onClick = { },
                            label = { Text(rarityTier.displayName) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
          // Cards list with optimized loading experience
        when {
            uiState.error != null -> {
                val errorMessage = uiState.error
                if (errorMessage != null) {
                    ErrorMessage(
                        message = errorMessage,
                        onRetry = viewModel::loadCards
                    )
                }
            }
            uiState.cards.isEmpty() && uiState.isLoading -> {
                // 首次加载显示完整的加载界面
                LoadingScreen()
            }
            else -> {
                // 显示卡片列表，如果在刷新则显示顶部加载指示器
                Column {
                    if (uiState.isLoading && uiState.cards.isNotEmpty()) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    if (uiState.cards.isEmpty()) {
                        EmptyState()
                    } else {
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
            }
        }        // Filter sheet
        CardFilterSheet(
            isVisible = showFilterSheet,
            onDismiss = { showFilterSheet = false },
            selectedAttribute = uiState.selectedAttribute,
            selectedRarityTier = uiState.selectedRarityTier,
            selectedSortOrder = uiState.sortOrder,
            showFavoritesOnly = uiState.showFavoritesOnly,
            isGridView = isGridView,
            onAttributeChange = viewModel::filterByAttribute,
            onRarityTierChange = viewModel::filterByRarityTier,
            onSortOrderChange = viewModel::setSortOrder,
            onFavoritesOnlyChange = viewModel::setShowFavoritesOnly,
            onViewModeChange = { isGridView = it },
            onClearFilters = viewModel::clearFilters
        )
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.loading_cards),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_cards),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.please_wait_or_refresh),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
