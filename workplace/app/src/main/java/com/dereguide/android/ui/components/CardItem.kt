package com.dereguide.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dereguide.android.data.model.Card
import com.dereguide.android.ui.theme.CoolColor
import com.dereguide.android.ui.theme.CuteColor
import com.dereguide.android.ui.theme.PassionColor

@Composable
fun CardItem(
    card: Card,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {            // Card image
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getAttributeColor(card.attribute)),
                contentAlignment = Alignment.Center
            ) {
                if (card.iconImageUrl != null) {
                    AsyncImage(
                        model = card.iconImageUrl,
                        contentDescription = card.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Show character name as placeholder
                    Text(
                        text = card.name.take(2),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Card info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Card name
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Rarity stars
                Text(
                    text = "★".repeat(card.rarity),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFD700) // Gold color for stars
                )
                
                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatChip(label = "V", value = card.vocal)
                    StatChip(label = "D", value = card.dance)
                    StatChip(label = "Vi", value = card.visual)
                }
            }
            
            // Attribute indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(getAttributeColor(card.attribute)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getAttributeSymbol(card.attribute),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StatChip(
    label: String,
    value: Int
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

private fun getAttributeColor(attribute: String): Color {
    return when (attribute.lowercase()) {
        "cute" -> CuteColor
        "cool" -> CoolColor
        "passion" -> PassionColor
        else -> Color.Gray
    }
}

private fun getAttributeSymbol(attribute: String): String {
    return when (attribute.lowercase()) {
        "cute" -> "Cu"
        "cool" -> "Co"
        "passion" -> "Pa"
        else -> "?"
    }
}
