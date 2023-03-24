package com.example.ct

//TODO this class manages several types of triggers. it also 'watches' the variables from the cache. on change, the triggers check if their conditions are met. if so, they call the notification manager with a specific message. furthermore, this class 'watches' the variables from the user manager, which decides whether the user is spark, facilitator, or signal. depending on this, the messages are different
class TriggerManager(private val userManager: UserManager, private val notificationManager: NotificationManager) {
    fun checkTriggers() {
        // Implement your logic to check for triggers.
        val triggered = isTriggered()

        if (triggered) {
            notificationManager.sendTakeAWalkNotification()
        }
    }

    private fun isTriggered(): Boolean {
        // Implement your logic to determine if a trigger condition is met.
        // Return true if the trigger condition is met, false otherwise.
        return false
    }
}
