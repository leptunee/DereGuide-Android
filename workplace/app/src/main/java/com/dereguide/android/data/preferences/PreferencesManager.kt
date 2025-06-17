package com.dereguide.android.data.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREF_NAME = "dereguide_preferences"
        private const val KEY_LAST_CARD_REFRESH_TIME = "last_card_refresh_time"
        private const val KEY_LAST_CHARACTER_REFRESH_TIME = "last_character_refresh_time"
        private const val KEY_LAST_SONG_REFRESH_TIME = "last_song_refresh_time"
        private const val KEY_AUTO_REFRESH_ENABLED = "auto_refresh_enabled"
        private const val KEY_CACHE_EXPIRY_HOURS = "cache_expiry_hours"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getLastCardRefreshTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_CARD_REFRESH_TIME, 0L)
    }

    fun setLastCardRefreshTime(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_CARD_REFRESH_TIME, timestamp)
            .apply()
    }

    fun getLastCharacterRefreshTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_CHARACTER_REFRESH_TIME, 0L)
    }

    fun setLastCharacterRefreshTime(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_CHARACTER_REFRESH_TIME, timestamp)
            .apply()
    }

    fun getLastSongRefreshTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_SONG_REFRESH_TIME, 0L)
    }

    fun setLastSongRefreshTime(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_SONG_REFRESH_TIME, timestamp)
            .apply()
    }

    fun isAutoRefreshEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_REFRESH_ENABLED, true)
    }

    fun setAutoRefreshEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_AUTO_REFRESH_ENABLED, enabled)
            .apply()
    }    fun getCacheExpiryHours(): Int {
        return sharedPreferences.getInt(KEY_CACHE_EXPIRY_HOURS, 24)
    }

    fun setCacheExpiryHours(hours: Int) {
        sharedPreferences.edit()
            .putInt(KEY_CACHE_EXPIRY_HOURS, hours)
            .apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}
