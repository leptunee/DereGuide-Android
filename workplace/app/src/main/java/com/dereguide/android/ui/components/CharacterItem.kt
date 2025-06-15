package com.dereguide.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.dereguide.android.data.model.Character
import com.dereguide.android.ui.theme.CoolColor
import com.dereguide.android.ui.theme.CuteColor
import com.dereguide.android.ui.theme.PassionColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showBirthdayBadge: Boolean = false
) {
    val isBirthdayToday = showBirthdayBadge && isBirthdayToday(character.birthday)
    
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
            // Character image with birthday badge
            Box {
                AsyncImage(
                    model = character.iconImageUrl,
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                if (isBirthdayToday) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.TopEnd)
                            .background(
                                Color(0xFFFFD700),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎂",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            // Character info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Character name
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Character name in kana
                character.nameKana?.let { kana ->
                    Text(
                        text = kana,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Age and birthday
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    character.age?.let { age ->
                        InfoChip(label = "Age", value = "$age")
                    }
                    character.birthday?.let { birthday ->
                        InfoChip(label = "Birthday", value = formatBirthday(birthday))
                    }
                }
                
                // Additional info
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    character.height?.let { height ->
                        InfoChip(label = "Height", value = "${height}cm")
                    }
                    character.hometown?.let { hometown ->
                        InfoChip(label = "From", value = hometown)
                    }
                }
            }
            
            // Attribute indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(getAttributeColor(character.attribute)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getAttributeSymbol(character.attribute),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
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

private fun formatBirthday(birthday: String): String {
    return try {
        val parts = birthday.split("-")
        if (parts.size >= 2) {
            "${parts[1]}/${parts[2] ?: ""}"
        } else {
            birthday
        }
    } catch (e: Exception) {
        birthday
    }
}

private fun isBirthdayToday(birthday: String?): Boolean {
    if (birthday == null) return false
    
    return try {
        val today = LocalDate.now()
        val todayFormatted = String.format("%02d-%02d", today.monthValue, today.dayOfMonth)
        birthday.endsWith(todayFormatted)
    } catch (e: Exception) {
        false
    }
}
