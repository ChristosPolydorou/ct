package com.example.ct

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
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
import javax.sql.DataSource

private const val PERMISSIONS_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {
//    private val timer = Timer()
//    private val triggerTimer = Timer()
    private lateinit var musicBrowser: MusicBrowser

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val checkPermission = hasPermissions(this, permissions)
        if (checkPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
        }

//        val serviceIntent = Intent(applicationContext, WalkReminderService::class.java)
//        applicationContext.startForegroundService(serviceIntent)
//        applicationContext.startService(serviceIntent)


//        val context = applicationContext
//        val cache = Cache()
//        val notificationManager = MyNotificationManager(this.applicationContext)
//        val dataSourceManager = DataSourceManager(cache, context)
//        val user = User(type = "signal")
//        val userManager = UserManager(user)
//        val triggerManager = TriggerManager(context,userManager, notificationManager, cache)
//        timer.schedule(dataSourceManager, 100000, 3000 * 1000)//00) //todo set this to once a day for the take a walk timer
//        triggerTimer.schedule(triggerManager, 100000, 3000 * 1000)//00)
    }


    override fun onDestroy() {
            super.onDestroy()
//            timer.cancel()
//            musicBrowser.disconnect()
        }

        //createNotificationChannel()

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

private fun hasPermissions(mainActivity: MainActivity, permissions: Array<String>): Array<String> {
            if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
                permissions.toMutableList().removeAt(1)
        }
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Request the permission
            permissions.toMutableList().removeAt(0)
        }

    return permissions
}
