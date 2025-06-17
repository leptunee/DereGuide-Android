package com.dereguide.android.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.*

class LocaleHelper(base: Context) : ContextWrapper(base) {

    companion object {
        fun setLocale(context: Context, language: String): Context {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                updateResources(context, language)
            } else {
                updateResourcesLegacy(context, language)
            }
        }

        private fun updateResources(context: Context, language: String): Context {
            val locale = getLocaleFromLanguageCode(language)
            Locale.setDefault(locale)

            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)

            return context.createConfigurationContext(configuration)
        }

        @Suppress("DEPRECATION")
        private fun updateResourcesLegacy(context: Context, language: String): Context {
            val locale = getLocaleFromLanguageCode(language)
            Locale.setDefault(locale)

            val resources = context.resources
            val configuration = resources.configuration
            configuration.locale = locale
            configuration.setLayoutDirection(locale)

            resources.updateConfiguration(configuration, resources.displayMetrics)
            return context
        }

        private fun getLocaleFromLanguageCode(language: String): Locale {
            return when (language) {
                "en" -> Locale.ENGLISH
                "zh" -> Locale.CHINESE
                "ja" -> Locale.JAPANESE
                else -> Locale.getDefault()
            }
        }
    }
}
