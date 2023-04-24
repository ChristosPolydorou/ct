package com.example.ct

import android.app.LocaleConfig
import android.app.LocaleManager
import android.content.Context
import java.security.AccessController.getContext

//TODO this class manages several types of triggers. Everytime when the variables from the cache change, the triggers check if their conditions are met. if so, they call the notification manager with a specific message. furthermore, this class 'watches' the variables from the user manager, which decides whether the user is spark, facilitator, or signal. depending on this, the messages are different
class TriggerManager(
    context: Context,
)  {
    private val notificationManager: MyNotificationManager = MyNotificationManager(context)
    private val sharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
    var userType = sharedPreferences.getString(R.string.user.toString(), UserType.UNKNOWN.type)

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
//                refreshCache(R.string.weather_is_good.toString())
            }
            R.string.five_pm.toString() -> {
                message = when (userType) {
                    UserType.SIGNAL.type -> "End of work means time for yourself! Let's take a quick walk to cool down after a long day."
                    UserType.FACILITATOR.type -> "End of work means time for yourself! Let's take a moderate walk to relax after a long day."
                    UserType.SPARK.type -> "End of work means time for yourself! Let's take a long walk to unwind after a long day."
                    else -> "End of work means time for yourself! Let's take a quick walk to cool down after a long day."
                }
                triggered = value.toString().toBoolean()
//                refreshCache(R.string.five_pm.toString())
            }
            R.string.calendar_is_empty.toString() -> {
                triggered = value.toString().toBoolean()
                message = when (userType) {
                    UserType.SIGNAL.type -> "You don't have anything planned today! Why not go for a short walk then?"
                    UserType.FACILITATOR.type -> "You don't have anything planned today! Why not go for a moderate walk then?"
                    UserType.SPARK.type -> "You don't have anything planned today! Why not go for a long walk then?"
                    else -> "You don't have anything planned today! Why not go for a walk then?"
                }
//                refreshCache(R.string.calendar_is_empty.toString())
            }
            "location_near_jogging_track" -> {
                triggered = value.toString().toBoolean()
                message = when (userType) {
                    UserType.SIGNAL.type -> "There is a park nearby! Why not take a short walk to explore it?"
                    UserType.FACILITATOR.type -> "There is a park nearby! Why not take a moderate walk to explore it?"
                    UserType.SPARK.type -> "There is a park nearby! Why not take a long walk to explore it?"
                    else -> "There is a park nearby! Why not take a walk to explore it?"
                }
//                refreshCache( "location_near_jogging_track")
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

    private fun refreshCache (key:String){
        val currentContext = getContext() as Context
         val cachePreferences = currentContext.getSharedPreferences(R.string.situations_cache.toString(),
            Context.MODE_PRIVATE)
        cachePreferences.edit().putBoolean(key, false).apply()
    }
}