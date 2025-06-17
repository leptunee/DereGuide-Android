package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dereguide.android.R
import com.dereguide.android.ui.viewmodel.CardStatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardStatsScreen(
    navController: NavController,
    viewModel: CardStatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("卡片统计") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.loading_failed),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = uiState.error!!)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadStats() }) {
                            Text("重试")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 总体统计
                    item {
                        StatsCard(
                            title = "总体统计",
                            stats = listOf(
                                "总卡片数" to uiState.totalCards.toString(),
                                "收藏卡片数" to uiState.favoriteCards.toString(),
                                "收藏率" to "${if (uiState.totalCards > 0) (uiState.favoriteCards * 100 / uiState.totalCards) else 0}%"
                            )
                        )
                    }
                    
                    // 按属性统计
                    item {
                        StatsCard(
                            title = "属性分布",
                            stats = listOf(
                                "Cute" to uiState.cuteCards.toString(),
                                "Cool" to uiState.coolCards.toString(),
                                "Passion" to uiState.passionCards.toString()
                            )
                        )
                    }
                    
                    // 按稀有度统计
                    item {
                        StatsCard(
                            title = "稀有度分布",
                            stats = listOf(
                                "★" to uiState.normalCards.toString(),
                                "★★" to uiState.rareCards.toString(),
                                "★★★" to uiState.superRareCards.toString()
                            )
                        )
                    }
                    
                    // 属性值统计
                    item {
                        StatsCard(
                            title = "最高属性值",
                            stats = listOf(
                                "最高Vocal" to uiState.maxVocal.toString(),
                                "最高Dance" to uiState.maxDance.toString(),
                                "最高Visual" to uiState.maxVisual.toString(),
                                "最高总值" to uiState.maxTotalStats.toString()
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(
    title: String,
    stats: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            stats.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
