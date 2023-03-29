package com.example.ct

import java.util.*

//TODO this class manages several types of triggers. it also 'watches' the variables from the cache. on change, the triggers check if their conditions are met. if so, they call the notification manager with a specific message. furthermore, this class 'watches' the variables from the user manager, which decides whether the user is spark, facilitator, or signal. depending on this, the messages are different
class TriggerManager(
    private val userManager: UserManager,
    private val notificationManager: MyNotificationManager,
    private val cache: Cache
    ): TimerTask() {

    fun checkTriggers() {
        //weather trigger
        var triggered = false
        var message = ""
        if (cache.get("weatherGood")) {
            message = "The weather is great! Let's go for a walk and use it."
            triggered = true
        }

        if (cache.get("FivePm")){
            message = ("End of work means time for yourself! Let's take a quick walk to cool down after a long day.")
            triggered = true
        }

        if (cache.get("calendarEmpty")){
            triggered = true
            message = "You don't have anything planned today! Why not go for a walk then?"
        }

        if (triggered){
            notificationManager.sendTakeAWalkNotification(message)
        }
    }

    override fun run(){
        checkTriggers()
    }


}
