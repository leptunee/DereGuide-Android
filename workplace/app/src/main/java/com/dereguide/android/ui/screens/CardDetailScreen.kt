package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dereguide.android.R
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
    
    Scaffold(        topBar = {
            TopAppBar(
                title = { Text(uiState.card?.name ?: "卡片详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    uiState.card?.let { card ->
                        IconButton(
                            onClick = { viewModel.toggleFavorite(card.id) }
                        ) {
                            Icon(
                                imageVector = if (card.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (card.isFavorite) "取消收藏" else "收藏",
                                tint = if (card.isFavorite) androidx.compose.ui.graphics.Color.Red else LocalContentColor.current
                            )
                        }
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
                    text = stringResource(R.string.basic_info),
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
        }        // Skills - 显示技能信息，即使为空也显示卡片
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.skill_info),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                if (!card.skill.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.skill_name), card.skill)
                } else {
                    InfoRow(stringResource(R.string.skill_name), "技能信息暂无")
                }
                
                if (!card.skillDescription.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.skill_effect), card.skillDescription)
                } else if (!card.skill.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.skill_effect), "技能效果详情暂无")
                }
                
                if (!card.centerSkill.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.center_skill_name), card.centerSkill)
                } else {
                    InfoRow(stringResource(R.string.center_skill_name), "中心技能暂无")
                }
                
                if (!card.centerSkillDescription.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.center_skill_effect), card.centerSkillDescription)
                } else if (!card.centerSkill.isNullOrEmpty()) {
                    InfoRow(stringResource(R.string.center_skill_effect), "中心技能效果详情暂无")
                }
                
                // 显示技能类型（如果有）
                card.skillType?.let { skillType ->
                    InfoRow("技能类型", skillType)
                }
            }
        }
        
        // Stats
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {                Text(
                    text = stringResource(R.string.attribute_values),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                InfoRow("Vocal", "${card.vocal} → ${card.vocal2 ?: card.vocal}")
                InfoRow("Dance", "${card.dance} → ${card.dance2 ?: card.dance}")
                InfoRow("Visual", "${card.visual} → ${card.visual2 ?: card.visual}")
                val baseTotal = card.vocal + card.dance + card.visual
                val maxTotal = (card.vocal2 ?: card.vocal) + (card.dance2 ?: card.dance) + (card.visual2 ?: card.visual)
                InfoRow("总和", "$baseTotal → $maxTotal")
                
                // 显示属性分布
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.attribute_distribution),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val maxStat = maxOf(card.vocal2 ?: card.vocal, card.dance2 ?: card.dance, card.visual2 ?: card.visual)
                StatBar("Vocal", card.vocal2 ?: card.vocal, maxStat)
                StatBar("Dance", card.dance2 ?: card.dance, maxStat)
                StatBar("Visual", card.visual2 ?: card.visual, maxStat)
            }
        }
    }
}

@Composable
private fun StatBar(
    label: String,
    value: Int,
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = if (maxValue > 0) value.toFloat() / maxValue.toFloat() else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = when (label) {
                "Vocal" -> MaterialTheme.colorScheme.primary
                "Dance" -> MaterialTheme.colorScheme.secondary
                "Visual" -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.primary
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
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
