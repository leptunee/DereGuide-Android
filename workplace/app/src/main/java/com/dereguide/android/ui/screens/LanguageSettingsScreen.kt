package com.dereguide.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dereguide.android.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(
    navController: NavController,
    currentLocale: String,
    onLanguageChanged: (String) -> Unit
) {
    val availableLanguages = listOf(
        Language("en", "English", "English"),
        Language("zh", "中文", "Chinese"),
        Language("ja", "日本語", "Japanese")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.language)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableLanguages) { language ->
                LanguageItem(
                    language = language,
                    isSelected = currentLocale == language.code,
                    onLanguageSelected = { onLanguageChanged(language.code) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onLanguageSelected: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onLanguageSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = language.nativeName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = language.englishName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

data class Language(
    val code: String,
    val nativeName: String,
    val englishName: String
)
