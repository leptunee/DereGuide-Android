package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dereguide.android.R
import com.dereguide.android.ui.components.CardGrid
import com.dereguide.android.ui.components.CardItem
import com.dereguide.android.ui.viewmodel.FavoriteCardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCardsScreen(
    navController: NavController,
    viewModel: FavoriteCardsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isGridView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("收藏的卡片") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isGridView = !isGridView }
                    ) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.List else Icons.Default.GridView,
                            contentDescription = if (isGridView) "列表视图" else "网格视图"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }                uiState.error != null -> {                    FavoriteErrorMessage(
                        message = uiState.error!!,
                        onRetry = viewModel::refreshFavoriteCards
                    )
                }
                uiState.favoriteCards.isEmpty() -> {
                    EmptyFavoritesMessage()
                }
                else -> {
                    if (isGridView) {
                        CardGrid(
                            cards = uiState.favoriteCards,
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
                            items(uiState.favoriteCards) { card ->
                                CardItem(
                                    card = card,
                                    onClick = {
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
    }
}

@Composable
private fun EmptyFavoritesMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💔",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_favorites),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.favorites_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FavoriteErrorMessage(
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
