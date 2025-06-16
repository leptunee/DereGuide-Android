package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dereguide.android.data.model.Card
import com.dereguide.android.ui.viewmodel.CardDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    navController: NavController,
    cardId: Int,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(cardId) {
        viewModel.loadCard(cardId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.card?.name ?: "卡片详情") },
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
                            text = "加载失败",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error!!,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCard(cardId) }) {
                            Text("重试")
                        }
                    }
                }
            }
            uiState.card != null -> {
                CardDetailContent(
                    card = uiState.card!!,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun CardDetailContent(
    card: Card,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card Image
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = card.imageUrl,
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                contentScale = ContentScale.Crop
            )
        }
        
        // Basic Info
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "基本信息",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                InfoRow("卡片名称", card.name)
                InfoRow("稀有度", "★".repeat(card.rarity))
                InfoRow("属性", card.attribute)
                InfoRow("最大等级", card.maxLevel.toString())
                card.maxLevel2?.let { maxLevel2 ->
                    if (maxLevel2 > card.maxLevel) {
                        InfoRow("觉醒后等级", maxLevel2.toString())
                    }
                }
            }
        }
          // Skills
        if (!card.skill.isNullOrEmpty()) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "技能信息",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    InfoRow("技能名称", card.skill)
                    card.skillDescription?.let { skillDesc ->
                        InfoRow("技能效果", skillDesc)
                    }
                    card.centerSkill?.let { centerSkill ->
                        InfoRow("中心技能", centerSkill)
                    }
                    card.centerSkillDescription?.let { centerSkillDesc ->
                        InfoRow("中心技能效果", centerSkillDesc)
                    }
                }
            }
        }
        
        // Stats
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "属性值",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                InfoRow("Vocal", "${card.vocal} → ${card.vocal2 ?: card.vocal}")
                InfoRow("Dance", "${card.dance} → ${card.dance2 ?: card.dance}")
                InfoRow("Visual", "${card.visual} → ${card.visual2 ?: card.visual}")
                val baseTotal = card.vocal + card.dance + card.visual
                val maxTotal = (card.vocal2 ?: card.vocal) + (card.dance2 ?: card.dance) + (card.visual2 ?: card.visual)
                InfoRow("总和", "$baseTotal → $maxTotal")
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
            fontWeight = FontWeight.Medium
        )
    }
}
