package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "Received BOOT_COMPLETED broadcast.")
            val serviceIntent = Intent(context, WalkReminderService::class.java)
            context.startForegroundService(serviceIntent)
            Log.d("BootReceiver", "Started WalkReminderService.")

        }
    }
}