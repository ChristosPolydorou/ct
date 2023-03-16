package com.example.ct

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class WalkReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Build and display the notification
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channel_id")
            //.setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Take a walk!")
            .setContentText("It's time to take a walk.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }
}
