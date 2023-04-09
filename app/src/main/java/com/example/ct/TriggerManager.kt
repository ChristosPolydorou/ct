package com.example.ct

import android.content.Context
import android.content.SharedPreferences
import java.util.*

//interface TriggerListener {
//    fun onTrigger(message: String)
//}

//TODO this class manages several types of triggers. Everytime when the variables from the cache change, the triggers check if their conditions are met. if so, they call the notification manager with a specific message. furthermore, this class 'watches' the variables from the user manager, which decides whether the user is spark, facilitator, or signal. depending on this, the messages are different
class TriggerManager(
//    private val context: Context,
    private val userManager: UserManager,
    private val notificationManager: MyNotificationManager,
//    private val cache: Cache
) {

//    private val calendarEmptyTrigger = CalendarEmptyTrigger()
//    private var listener: TriggerListener? = null

//    init {
//        calendarEmptyTrigger.setTriggerListener(object : TriggerListener {
//            override fun onTrigger(message: String) {
//                sendNotification(message)
//            }
//        })
//    }

//
//    private fun sendNotification(message: String) {
//        listener?.onTrigger(message)
//    }

    fun checkTriggers(key:String, value:Any) {
        var triggered = false
        var message = ""
        when(key){
            R.string.weather_is_good.toString()-> {
                    message = "The weather is great! Let's go for a walk and use it."
                    triggered = value.toString().toBoolean()
            }
            R.string.five_pm.toString() -> {
                    message = ("End of work means time for yourself! Let's take a quick walk to cool down after a long day.")
                    triggered = value.toString().toBoolean()

            }
            R.string.calendar_is_empty.toString() -> {
                triggered = value.toString().toBoolean()
                message = "You don't have anything planned today! Why not go for a walk then?"
            }
            else -> {
                message = ("This is a test trigger.")
                triggered = true
            }
        }

        if (triggered){
            notificationManager.sendTakeAWalkNotification(message)
        }
    }

    fun checkMusicTrigger() {
        // Implement your logic to handle the rock music trigger
        val shouldNotify = userManager.shouldSendRockMusicNotification()

        if (shouldNotify) {
            notificationManager.sendRockMusicNotification()
        }
    }

}
