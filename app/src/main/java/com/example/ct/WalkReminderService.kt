package com.example.ct

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import android.telecom.Call
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class WalkReminderService : Service() {
    private var reminderTimer: Timer? = null
    private var weatherTimer: Timer? = null
    private var isWalking = false
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startWeatherUpdates()
        startReminderTimer()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Walk Reminder Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.setDescription("Notifications for reminding the user to take a walk")
        channel.enableLights(true)
        channel.setLightColor(Color.GREEN)
        channel.enableVibration(true)
        val manager: NotificationManager = getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(channel)
    }

    private fun startWeatherUpdates() {
        val client = OkHttpClient()
        val request: DownloadManager.Request = Builder()
            .url("http://api.openweathermap.org/data/2.5/weather?q=LosAngeles,us&appid=" + OPEN_WEATHER_MAP_API_KEY)
            .build()
        weatherTimer = Timer()
        weatherTimer!!.schedule(object : TimerTask() {
            override fun run() {
                client.newCall(request).enqueue(object : Callback() {
                    fun onFailure(call: Call?, e: IOException) {
                        e.printStackTrace()
                    }

                    @Throws(IOException::class)
                    fun onResponse(call: Call?, response: Response) {
                        try {
                            val json = JSONObject(response.body().string())
                            val temperature: Double = json.getJSONObject("main").getDouble("temp")
                            val isSunny = json.getJSONArray("weather").getJSONObject(0)
                                .getString("main") == "Clear"
                            isWalking = temperature > 20 && isSunny
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                })
            }
        }, 0, WEATHER_UPDATE_INTERVAL.toLong())
    }

    private fun startReminderTimer() {
        reminderTimer = Timer()
        reminderTimer!!.schedule(object : TimerTask() {
            override fun run() {
                if (isWalking) {
                    val notification: Notification = NotificationCompat.Builder(
                        applicationContext, CHANNEL_ID
                    ) //.setSmallIcon(R.drawable.ic_walk)
                        .setContentTitle("Take a walk!")
                        .setContentText("The weather is nice, go for a walk!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build()
                    val manager: NotificationManagerCompat = NotificationManagerCompat.from(
                        applicationContext
                    )
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    manager.notify(NOTIFICATION_ID, notification)
                }
            }
        }, 0, REMINDER_INTERVAL.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        stopWeatherUpdates()
        stopReminderTimer()
    }

    private fun stopWeatherUpdates() {
        weatherTimer!!.cancel()
        weatherTimer!!.purge()
    }

    private fun stopReminderTimer() {
        reminderTimer!!.cancel()
        reminderTimer!!.purge()
    }

    companion object {
        private const val CHANNEL_ID = "Walk Reminder Channel"
        private const val NOTIFICATION_ID = 1
        private const val OPEN_WEATHER_MAP_API_KEY = "YOUR_API_KEY_HERE"
        private const val REMINDER_INTERVAL = 30 * 60 * 1000 // 30 minutes
        private const val WEATHER_UPDATE_INTERVAL = 5 * 60 * 1000 // 5 minutes
    }
}