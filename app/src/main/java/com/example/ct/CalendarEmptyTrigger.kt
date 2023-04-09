package com.example.ct


import android.content.Context

//TODO this class should actually check if the calendarEmptyTrigger is triggered, and then pass the fitting message back to the TriggerManager, which in turn passes the not to the NotificationManager
interface TriggerListener {
    fun onTrigger(message: String)
}

class CalendarEmptyTrigger {
    private var listener: TriggerListener? = null

    fun setTriggerListener(listener: TriggerListener) {
        this.listener = listener
    }

    fun checkCalendarEmpty(context: Context) {
        val calendarEmpty = isCalendarEmpty(context)
        if (calendarEmpty) {
            trigger()
        }
    }
    private fun isCalendarEmpty(context: Context): Boolean {
        // TODO: Implement the logic to check if the calendar is empty
        return true // Replace this with the actual check
    }

    private fun trigger() {
        listener?.onTrigger("It's time to take a walk!")
    }
}
