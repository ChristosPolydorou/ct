package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log


// TODO  It'll take a while to get to this function. If the foreground service starts in the MainActivity, it'll faster.
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {

//        val serviceIntent = Intent(context, WalkReminderService::class.java)
//        context.startForegroundService(serviceIntent)
//        context.startService(serviceIntent)         // I think startService is more suitable here.
        Log.d("==============","================================")
//        }
    }
}