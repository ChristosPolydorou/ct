package com.example.ct

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

private const val PERMISSIONS_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions( this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE
            )
        }

        createNotificationChannel()

        /*val serviceIntent = Intent(this, WalkReminderService::class.java)
        startService(serviceIntent)*/

//        // Get a Calendar instance for the current time
//        val calendar: Calendar = Calendar.getInstance()
//
//        // Set the time to trigger the notification (in this example, 8:00 AM)
//        calendar.set(Calendar.HOUR_OF_DAY, 8)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//
//        // Create a PendingIntent for the WalkReminderService
//        val intent = Intent(this, WalkReminderService::class.java)
//        val pendingIntent =
//            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // Get an instance of AlarmManager
//
//        // Get an instance of AlarmManager
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//
//        // Set the alarm to trigger at the specified time
//
//        // Set the alarm to trigger at the specified time
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.getTimeInMillis(),
//            AlarmManager.INTERVAL_DAY,
//            pendingIntent
//        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Take a Walk Channel"
            val descriptionText = "Notifications for taking a walk"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(MyNotificationManager.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

