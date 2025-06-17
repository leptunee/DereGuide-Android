package com.dereguide.android.data.preferences

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    companion object {
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
        private const val DEFAULT_LANGUAGE = "system"
    }

    fun getCurrentLanguage(): String {
        return preferencesManager.getString(KEY_SELECTED_LANGUAGE, DEFAULT_LANGUAGE)
    }

    fun setLanguage(languageCode: String) {
        preferencesManager.setString(KEY_SELECTED_LANGUAGE, languageCode)
    }

    fun getSystemLanguage(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale.language
        }
    }

    fun applyLanguage(context: Context, languageCode: String): Context {
        val locale = when (languageCode) {
            "system" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.resources.configuration.locales[0]
                } else {
                    @Suppress("DEPRECATION")
                    context.resources.configuration.locale
                }
            }
            "en" -> Locale.ENGLISH
            "zh" -> Locale.CHINESE
            "ja" -> Locale.JAPANESE
            else -> Locale.ENGLISH
        }

        Locale.setDefault(locale)
        
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        return context.createConfigurationContext(configuration)
    }

    fun getSupportedLanguages(): List<SupportedLanguage> {
        return listOf(
            SupportedLanguage("system", "跟随系统", "Follow System"),
            SupportedLanguage("en", "English", "English"),
            SupportedLanguage("zh", "中文", "Chinese"),
            SupportedLanguage("ja", "日本語", "Japanese")
        )
    }

    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "system" -> "Follow System"
            "en" -> "English"
            "zh" -> "中文"
            "ja" -> "日本語"
            else -> "Unknown"
        }
    }
}

data class SupportedLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String
)
