package com.dereguide.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dereguide.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardFilterSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    selectedAttribute: String?,
    selectedRarity: Int?,
    selectedSortOrder: SortOrder,
    showFavoritesOnly: Boolean,
    onAttributeChange: (String?) -> Unit,
    onRarityChange: (Int?) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onFavoritesOnlyChange: (Boolean) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "筛选和排序",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    TextButton(
                        onClick = onClearFilters
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("清除")
                    }
                }
                
                Divider()
                
                // 收藏筛选
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "只显示收藏",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = showFavoritesOnly,
                        onCheckedChange = onFavoritesOnlyChange
                    )
                }
                  // 属性筛选
                Column {
                    Text(
                        text = "属性",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedAttribute == null,
                            onClick = { onAttributeChange(null) },
                            label = { Text("全部") }
                        )
                        FilterChip(
                            selected = selectedAttribute == "cute",
                            onClick = { onAttributeChange("cute") },
                            label = { Text("Cute") }
                        )
                        FilterChip(
                            selected = selectedAttribute == "cool",
                            onClick = { onAttributeChange("cool") },
                            label = { Text("Cool") }
                        )
                        FilterChip(
                            selected = selectedAttribute == "passion",
                            onClick = { onAttributeChange("passion") },
                            label = { Text("Passion") }
                        )
                    }
                }
                  // 稀有度筛选
                Column {
                    Text(
                        text = "稀有度",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedRarity == null,
                            onClick = { onRarityChange(null) },
                            label = { Text("全部") }
                        )
                        repeat(3) { index ->
                            val rarity = index + 1
                            FilterChip(
                                selected = selectedRarity == rarity,
                                onClick = { onRarityChange(rarity) },
                                label = { Text("★".repeat(rarity)) }
                            )
                        }
                    }
                }
                
                // 排序选项
                Column {
                    Text(
                        text = "排序方式",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SortOrder.values().forEach { sortOrder ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedSortOrder == sortOrder,
                                onClick = { onSortOrderChange(sortOrder) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = sortOrder.displayName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

enum class SortOrder(val displayName: String) {
    DEFAULT("默认"),
    TOTAL_STATS_DESC("总属性值（高到低）"),
    TOTAL_STATS_ASC("总属性值（低到高）"),
    VOCAL_DESC("Vocal（高到低）"),
    DANCE_DESC("Dance（高到低）"),
    VISUAL_DESC("Visual（高到低）"),
    RARITY_DESC("稀有度（高到低）"),
    RARITY_ASC("稀有度（低到高）"),
    NAME_ASC("名称（A-Z）"),
    RELEASE_DATE_DESC("发布时间（新到旧）")
}
