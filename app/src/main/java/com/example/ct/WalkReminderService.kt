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
    private lateinit var pendingIntent1 : PendingIntent
    private lateinit var pendingIntent2 : PendingIntent
    private val scope = CoroutineScope(Dispatchers.Default)

    private lateinit var user: User
    private lateinit var userManager: UserManager
    private lateinit var notificationManager: MyNotificationManager
    private lateinit var triggerManager: TriggerManager
    private lateinit var cache: Cache

    private lateinit var weatherData: DataSourceManager
    private lateinit var calendarData: DataSourceManager
    private lateinit var locationData: DataSourceManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        user = User("signal")
        userManager = UserManager(user, this)
        notificationManager = MyNotificationManager(this)
        triggerManager = TriggerManager(userManager, notificationManager)
        cache = Cache(this, triggerManager)
        weatherData = WeatherDataSource(cache, this)
        calendarData = CalendarDataSource(cache, this)
        locationData = GeolocationDataSource(cache,  this)
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

        //  Create a notification for the foreground service
        val notificationManagForService = MyNotificationManager(this)
        val notificationForService = notificationManagForService.sendServiceNotification()
        startForeground(1, notificationForService)

        // Create an Intent for the alarm that updates data(weather, location etc.) every certain time
        // and pass the instances to the Receiver class
        val intent1 = Intent(this, WalkReminderReceiver::class.java).apply {
            putExtra("cache", cache)
            putExtra("weather", weatherData)
//            putExtra("calender", calendarData)
//            putExtra("location", locationData)
        }
        // Create an Intent for the alarm that triggers 5pm notification and pass the instances
        val intent2 = Intent(this, WalkReminderReceiver::class.java).apply {
            putExtra("cache", cache)
            putExtra("weather", weatherData)
//            putExtra("calender", calendarData)
//            putExtra("location", locationData)
        }
        scope.launch {
            // Get the AlarmManager service
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            intent1.action = "Action_For_Load_Data"
            pendingIntent1 = PendingIntent.getBroadcast(applicationContext, 0, intent1, 0)

            // TODO Setting the alarm to update data every certain time.
            var interval: Long = 60 * 1000
            var firstAlarmTime: Long = System.currentTimeMillis() + interval
            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstAlarmTime,
                interval,
                pendingIntent1
            )

            intent2.action = "Action_For_Five_Pm"
            pendingIntent2 = PendingIntent.getBroadcast(applicationContext, 0, intent2, 0)

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
        super.onDestroy()
        scope.cancel()
        stopReminderTimer()
    }

//    private fun stopWeatherUpdates() {
//        weatherTimer.cancel()
//        weatherTimer.purge()
//    }

    private fun stopReminderTimer() {
        alarmManager.cancel(pendingIntent1)
        alarmManager.cancel(pendingIntent2)
    }

}
