package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log


// I'm not sure if this is working cuz the Log.d message didn't show
class BootReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_CONTEXT = "com.example.myapp.EXTRA_CONTEXT"
    }
    override fun onReceive(context: Context, intent: Intent) {
//        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {

        // using the application context instead of the receiver context to avoid leaking the receiver context.
        val serviceIntent = Intent(context, WalkReminderService::class.java)
        context.startForegroundService(serviceIntent)
        Log.d("==============","================================")
//        }
    }
}