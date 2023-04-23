package com.example.ct

import android.content.Context
import android.content.SharedPreferences
import android.location.LocationListener
import java.io.Serializable

//interface TriggerListener {
//    fun onTrigger(message: String)
//}
//TODO this class manages several types of triggers. Everytime when the variables from the cache change, the triggers check if their conditions are met. if so, they call the notification manager with a specific message. furthermore, this class 'watches' the variables from the user manager, which decides whether the user is spark, facilitator, or signal. depending on this, the messages are different
class TriggerManager(
    context: Context,
//    private val userManager: UserManager,
//    private val notificationManager: MyNotificationManager,
//    private val cache: Cache
)  {
    private val notificationManager: MyNotificationManager = MyNotificationManager(context)
    private val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    var userType = sharedPreferences.getString(R.string.user.toString(), UserType.UNKNOWN.type)
    /*private val calendarEmptyTrigger = CalendarEmptyTrigger()*/
    /*init {
        calendarEmptyTrigger.setTriggerListener(object : TriggerListener {
            override fun onTrigger(message: String) {
                sendNotification(message)
            }
        })
    }*/
//    private var listener: TriggerListener? = null

//    private fun sendNotification(message: String) {
//        listener?.onTrigger(message)
//    }


    fun checkTriggers(key: String, value: Any) {
        var triggered = false
        var message = ""
        when (key) {
            R.string.weather_is_good.toString() -> {
                message = when (userType) {
                    UserType.SIGNAL.type -> "The weather is great! Let's go for a short walk and enjoy it."
                    UserType.FACILITATOR.type -> "The weather is great! Let's go for a moderate walk and use it."
                    UserType.SPARK.type -> "The weather is great! Let's go for a long walk and use it."
                    else -> "The weather is great! Let's go for a walk and use it."
                }
                triggered = value.toString().toBoolean()
            }
            R.string.five_pm.toString() -> {
                message = when (userType) {
                    UserType.SIGNAL.type -> "End of work means time for yourself! Let's take a quick walk to cool down after a long day."
                    UserType.FACILITATOR.type -> "End of work means time for yourself! Let's take a moderate walk to relax after a long day."
                    UserType.SPARK.type -> "End of work means time for yourself! Let's take a long walk to unwind after a long day."
                    else -> "End of work means time for yourself! Let's take a quick walk to cool down after a long day."
                }
                triggered = value.toString().toBoolean()
                Cache.put(R.string.five_pm.toString(), false)
            }
            R.string.calendar_is_empty.toString() -> {
                triggered = value.toString().toBoolean()
                message = when (userType) {
                    UserType.SIGNAL.type -> "You don't have anything planned today! Why not go for a short walk then?"
                    UserType.FACILITATOR.type -> "You don't have anything planned today! Why not go for a moderate walk then?"
                    UserType.SPARK.type -> "You don't have anything planned today! Why not go for a long walk then?"
                    else -> "You don't have anything planned today! Why not go for a walk then?"
                }
            }
            R.string.location_near.toString() -> {
                triggered = value.toString().toBoolean()
                message = when (userType) {
                    UserType.SIGNAL.type -> "There is a park nearby! Why not take a short walk to explore it?"
                    UserType.FACILITATOR.type -> "There is a park nearby! Why not take a moderate walk to explore it?"
                    UserType.SPARK.type -> "There is a park nearby! Why not take a long walk to explore it?"
                    else -> "There is a park nearby! Why not take a walk to explore it?"
                }
            }
            else -> {
                message = ("This is a test trigger.")
                triggered = true
            }
        }

        if (triggered) {
            notificationManager.sendTakeAWalkNotification(message)
//            sendNotification(message)
        } else {
            null
        }
    }

    fun checkMusicTrigger() {
        // Implement your logic to handle the rock music trigger
        val shouldNotify = sharedPreferences.getBoolean("rock_music_notifications_enabled", false)
//        val shouldNotify = userManager.shouldSendRockMusicNotification()

        if (shouldNotify) {
            notificationManager.sendRockMusicNotification()
        }
    }
}