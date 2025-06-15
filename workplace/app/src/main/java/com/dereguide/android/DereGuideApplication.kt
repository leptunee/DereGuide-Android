package com.dereguide.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DereGuideApplication : Application() {

    companion object {
        const val BIRTHDAY_CHANNEL_ID = "birthday_reminders"
        const val BIRTHDAY_CHANNEL_NAME = "Birthday Reminders"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        initializeWorkManager()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            
            // Birthday reminder channel
            val birthdayChannel = NotificationChannel(
                BIRTHDAY_CHANNEL_ID,
                BIRTHDAY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for idol birthdays"
            }
            
            notificationManager.createNotificationChannel(birthdayChannel)
        }
    }

    private fun initializeWorkManager() {
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()
        )
    }
}
