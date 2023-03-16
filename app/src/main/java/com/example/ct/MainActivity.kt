package com.example.ct

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
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

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_CODE);
        } else {
            // Permission has already been granted
            // Start the WalkReminderService
            startWalkReminderService();
        }

        // Get a Calendar instance for the current time
        // Get a Calendar instance for the current time
        val calendar: Calendar = Calendar.getInstance()

        // Set the time to trigger the notification (in this example, 8:00 AM)

        // Set the time to trigger the notification (in this example, 8:00 AM)
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Create a PendingIntent for the WalkReminderService

        // Create a PendingIntent for the WalkReminderService
        val intent = Intent(this, WalkReminderService::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Get an instance of AlarmManager

        // Get an instance of AlarmManager
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Set the alarm to trigger at the specified time

        // Set the alarm to trigger at the specified time
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun startWalkReminderService() {
        // Get the AlarmManager service
        // Get the AlarmManager service
        val alarmManager = getSystemService<Any>(Context.ALARM_SERVICE) as AlarmManager

        // Create a PendingIntent for the notification

        // Create a PendingIntent for the notification
        val notificationIntent = Intent(this, WalkReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0)

        // Set the notification to trigger at 5 PM every day

        // Set the notification to trigger at 5 PM every day
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }
}