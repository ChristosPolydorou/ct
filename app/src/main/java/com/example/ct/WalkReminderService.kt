package com.example.ct

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*


class WalkReminderService : Service() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent1: PendingIntent
    private lateinit var pendingIntent2: PendingIntent
    private val scopeForAlarm = CoroutineScope(Dispatchers.Default)

    //scopeForLoadData is used for loading data from internet in the datasource classes
    // to avoid blocking the main thread
    companion object {
        private val scopeForLoadData = CoroutineScope(Dispatchers.Default)
        fun getScopeForLoadData(): CoroutineScope {
            return scopeForLoadData
        }
    }

    private lateinit var user: User
    private lateinit var userManager: UserManager

    private lateinit var weatherData: DataSourceManager
    private lateinit var calendarData: DataSourceManager
    private lateinit var locationData: DataSourceManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        user = User(UserType.SIGNAL)
        userManager = UserManager(user, this)
        Cache.initializeCache(this)
        weatherData = WeatherDataSource()
        calendarData = CalendarDataSource()
        locationData = GeolocationDataSource()

        //  Create a notification for the foreground service
        val notificationManagForService = MyNotificationManager(this)
        val notificationForService = notificationManagForService.sendServiceNotification()
        startForeground(1, notificationForService)

        scopeForAlarm.launch {
            // Create an Intent for the alarm that updates data(weather, location etc.) every certain time
            // and pass the instances to the Receiver class
            val intent1 = Intent(applicationContext, WalkReminderReceiver::class.java).apply {
                putExtra("weather", weatherData)
            putExtra("calendar", calendarData)
            putExtra("location", locationData)
            }
            // Create an Intent for the alarm that triggers 5pm notification and pass the instances
            val intent2 = Intent(applicationContext, WalkReminderReceiver::class.java).apply {
                putExtra("weather", weatherData)
            putExtra("calendar", calendarData)
            putExtra("location", locationData)
            }
            // Get the AlarmManager service
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            intent1.action = "Action_For_Load_Data"
            pendingIntent1 = PendingIntent.getBroadcast(applicationContext, 0, intent1, PendingIntent.FLAG_IMMUTABLE)

            // TODO Setting the alarm to update data every certain time.
            var interval: Long = 60 * 1000
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                5000,
                interval,
                pendingIntent1
            )

            intent2.action = "Action_For_Five_Pm"
            pendingIntent2 = PendingIntent.getBroadcast(applicationContext, 0, intent2, PendingIntent.FLAG_IMMUTABLE)

            // TODO Setting the alarm to trigger at 5pm every day.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar[Calendar.HOUR_OF_DAY] = 17
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent2
            )
        }

        // TODO In class WalkReminderReceiver it shows what will do every time when alarms are fired.

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        scopeForLoadData.cancel()
        stopReminderTimer()
        scopeForAlarm.cancel()
        super.onDestroy()

    }

    private fun stopReminderTimer() {
        alarmManager.cancel(pendingIntent1)
        alarmManager.cancel(pendingIntent2)
    }

}
