package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class WalkReminderReceiver : BroadcastReceiver() {
    //    private var facilitator = false //high motivation, low ability
//    private var signal = true //high ability, high motivation
    //private var spark = false //high ability, low motivation
    private lateinit var user: User
    private lateinit var userManager: UserManager
    private lateinit var notificationManager: MyNotificationManager
    private lateinit var triggerManager: TriggerManager
    private lateinit var cache: Cache

    private lateinit var weatherData: DataSourceManager
    private lateinit var calendarData: DataSourceManager
    private lateinit var locationData: DataSourceManager
    var testNum = 0 // for testing


    override fun onReceive(context: Context, intent: Intent) {

        user = User("signal")
        userManager = UserManager(user, context)
        notificationManager = MyNotificationManager(context)
        triggerManager = TriggerManager(userManager, notificationManager)
        cache = Cache(context, triggerManager)
        weatherData = WeatherDataSource(cache, context)
        calendarData = CalendarDataSource(cache, context)
        locationData = GeolocationDataSource(cache, context)

        if (intent.action == "Action_For_Load_Data") {
//--------------for testing--------------------------------//
            cache.set("steps", testNum)
            testNum++
            Log.d("steps:=============", testNum.toString())
//---------------------for testing-----------------------//
            weatherData.loadData()
            calendarData.loadData()
        } else if (intent.action == "Action_For_Five_Pm") {
            cache.set("FivePm", true)
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
