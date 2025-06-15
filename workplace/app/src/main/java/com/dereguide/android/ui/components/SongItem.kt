package com.dereguide.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import com.dereguide.android.data.model.Song
import com.dereguide.android.ui.theme.CoolColor
import com.dereguide.android.ui.theme.CuteColor
import com.dereguide.android.ui.theme.PassionColor

@Composable
fun SongItem(
    song: Song,
    onClick: () -> Unit,
    onPlayClick: (() -> Unit)? = null,
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
        ) {
            // Song jacket
            Box {
                AsyncImage(
                    model = song.jacketUrl,
                    contentDescription = song.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                
                // Play button overlay
                if (onPlayClick != null && song.previewUrl != null) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onPlayClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Preview",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            // Song info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Song name
                Text(
                    text = song.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Artist
                song.artist?.let { artist ->
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Song details
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    song.bpm?.let { bpm ->
                        SongInfoChip(label = "BPM", value = bpm.toString())
                    }
                    song.duration?.let { duration ->
                        SongInfoChip(label = "Duration", value = formatDuration(duration))
                    }
                }
                
                // Difficulty levels
                song.difficulty?.let { difficulties ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        difficulties.forEach { (level, value) ->
                            DifficultyChip(level = level, value = value)
                        }
                    }
                }
            }
            
            // Attribute indicator
            song.attribute?.let { attribute ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(getAttributeColor(attribute)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getAttributeSymbol(attribute),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SongInfoChip(
    label: String,
    value: String
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "$label: $value",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun DifficultyChip(
    level: String,
    value: Int
) {
    val color = when (level.lowercase()) {
        "debut" -> Color(0xFF4CAF50)
        "regular" -> Color(0xFF2196F3)
        "pro" -> Color(0xFFFF9800)
        "master" -> Color(0xFFF44336)
        "master+" -> Color(0xFF9C27B0)
        else -> Color.Gray
    }
    
    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "$value",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
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
