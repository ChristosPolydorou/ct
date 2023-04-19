package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class WalkReminderReceiver : BroadcastReceiver() {
    //    private var facilitator = false //high motivation, low ability
//    private var signal = true //high ability, high motivation
    //private var spark = false //high ability, low motivation

    var testNum = 0 // for testing


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val cache = intent.getSerializableExtra("cache", Cache::class.java)
        val weatherData = intent.getSerializableExtra("weather", WeatherDataSource::class.java)
//        val calendarData = intent.getSerializableExtra("calendar", CalendarDataSource::class.java)
//        val locationData = intent.getSerializableExtra("location", GeolocationDataSource::class.java)

        if (intent.action == "Action_For_Load_Data") {
            weatherData!!.loadData()
//            calendarData!!.loadData()
//            locationData!!.loadData()
        } else if (intent.action == "Action_For_Five_Pm") {
            cache!!.set("FivePm", true)
        }


        // Build and display the notification
//        var notText = ""
//        if (intent.action == "weather") {
//            if (facilitator) {
//                notText =
//                    "The conditions outside are perfect, let's use this chance and take an easy walk!"
//            } else if (signal) {
//                notText = "The weather is amazing! Let's go for a walk!"
//            } else {
//                notText =
//                    "Let's make use of these amazing weather conditions and get some steps in!"
//            }
//        } else if (intent.action == "time"){
//            if (facilitator) {
//                notText =
//                    "It's 5pm. Maybe you can get some extra steps in before you settle onto the couch?"
//            } else if (signal) {
//                notText = "It's 5pm. This is a reminder that you can surpass yourself and get some extra steps in!"
//            } else {
//                notText =
//                    "It's 5pm. Let's get some extra steps in before you settle in for the day!"
//            }
//        }
//
//        val builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(context, "Walk Reminder Channel")
//                .setSmallIcon(R.drawable.ic_lock_idle_alarm)
//                .setContentTitle("Take a walk!")
//                .setContentText(notText)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        notificationManager.notify(1, builder.build())

    }
}
