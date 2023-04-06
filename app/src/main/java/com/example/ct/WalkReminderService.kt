package com.example.ct

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*


class WalkReminderService : Service() {
//    private var reminderTimer: Timer = Timer()
    private val timer = Timer()
    private val triggerTimer = Timer()
    // Get the AlarmManager service
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    private var weatherTimer: Timer = Timer()
//    private var isWalking = false

    override fun onCreate() {
        super.onCreate()
//        val context = applicationContext
//        val cache = Cache(context)
//        val notificationManager = MyNotificationManager(this.applicationContext)
//        val dataSourceManager : DataSourceManager = WeatherDataSource(cache, context)
//        val user = User(type = "signal")
//        val userManager = UserManager(user)
//        val triggerManager = TriggerManager(context,userManager, notificationManager, cache)
//        // Update data every certain time
//        timer.schedule(dataSourceManager, 100000, 3000 * 1000)
//        // Check Cache every certain time
//        triggerTimer.schedule(triggerManager, 100000, 3000 * 1000)

        // Create an Intent for the alarm that updates data(weather, location etc.) every certain time.
        val intent1 = Intent(this, WalkReminderReceiver::class.java)
        intent1.action = "Action_For_Load_Data"
        val pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0)

        // Setting the alarm to update data every certain time.
        var interval : Long = 3000*100000
        var firstAlarmTime : Long = System.currentTimeMillis()+interval
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstAlarmTime, interval, pendingIntent1)

        // Create an Intent for the alarm that triggers 5pm notification.
        val intent2 = Intent(this, WalkReminderReceiver::class.java)
        intent2.action = "Action_For_Five_Pm"
        val pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0)

        // Setting the alarm to trigger at 5pm every day.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 17
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent2)
        // In class WalkReminderReceiver it shows what will do every time when alarms are fired.
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Check for location permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Notify by weather
////            startWeatherUpdates()
////            startReminderTimer()
//            // Notify by location
//        }
//        fivePmNotification()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


//    private fun startWeatherUpdates() {
//
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url("http://api.openweathermap.org/data/2.5/weather?q=LosAngeles,us&appid=" + OPEN_WEATHER_MAP_API_KEY)
//            .build()
////        weatherTimer = Timer()
//        weatherTimer.schedule(object : TimerTask() {
//            override fun run() {
//                client.newCall(request).enqueue(object : Callback {
//                    override fun onFailure(call: okhttp3.Call, e: IOException) {
//                        e.printStackTrace()
//                    }
//
//                    override fun onResponse(call: okhttp3.Call, response: Response) {
//                        try {
//                            val responseBody = response.body
//                            val json = JSONObject(responseBody?.string())
//                            val temperature: Double = json.getJSONObject("main").getDouble("temp")
//                            val isSunny = json.getJSONArray("weather").getJSONObject(0)
//                                .getString("main") == "Clear"
//                            isWalking = temperature > 20 && isSunny
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//                    }
//                })
//            }
//        }, 0, WEATHER_UPDATE_INTERVAL.toLong())
//    }

    private fun checkUserCharacteristics(){
        //TODO implement checks which kind of user the user is
    }

//    private fun startReminderTimer() {
////        reminderTimer = Timer()
//        reminderTimer.schedule(object : TimerTask() {
//            override fun run() {
//                if (isWalking) {
//                    val activityIntent = Intent(applicationContext, MainActivity::class.java)
//                    val notification: Notification = NotificationCompat.Builder(
//                        applicationContext, CHANNEL_ID
//                    )
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentTitle("Take a walk!")
//                        .setContentText("Let's go for a walk!")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                        .setAutoCancel(true)
//                        .build()
//                    val manager: NotificationManagerCompat = NotificationManagerCompat.from(
//                        applicationContext
//                    )
//                    if (ActivityCompat.checkSelfPermission(
//                            applicationContext,
//                            Manifest.permission.POST_NOTIFICATIONS
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return
//                    }
//                    manager.notify(NOTIFICATION_ID, notification)
//                }
//            }
//        }, 0, REMINDER_INTERVAL.toLong())
//    }



    override fun onDestroy() {
        super.onDestroy()
//        stopWeatherUpdates()
        stopReminderTimer()
    }

//    private fun stopWeatherUpdates() {
//        weatherTimer.cancel()
//        weatherTimer.purge()
//    }

    private fun stopReminderTimer() {
//        reminderTimer.cancel()
//        reminderTimer.purge()
        timer.cancel()
        timer.purge()
        triggerTimer.cancel()
        triggerTimer.purge()
    }

    companion object {
        private const val CHANNEL_ID = "Walk Reminder Channel"
//        private const val NOTIFICATION_ID = 1
//        private const val OPEN_WEATHER_MAP_API_KEY = "YOUR_API_KEY_HERE"
//        private const val REMINDER_INTERVAL = 30 * 60 * 1000 // 30 minutes
//        private const val WEATHER_UPDATE_INTERVAL = 5 * 60 * 1000 // 5 minutes
    }
}
