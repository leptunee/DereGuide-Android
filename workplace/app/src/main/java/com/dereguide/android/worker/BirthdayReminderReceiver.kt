package com.dereguide.android.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dereguide.android.DereGuideApplication
import com.dereguide.android.R
import com.dereguide.android.ui.MainActivity

class BirthdayReminderReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val characterName = intent.getStringExtra("character_name") ?: return
        val characterId = intent.getIntExtra("character_id", -1)
        
        showBirthdayNotification(context, characterName, characterId)
    }
    
    private fun showBirthdayNotification(context: Context, characterName: String, characterId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("character_id", characterId)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            characterId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, DereGuideApplication.BIRTHDAY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🎂 Happy Birthday!")
            .setContentText("Today is $characterName's birthday!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(characterId, notification)
    }
}
