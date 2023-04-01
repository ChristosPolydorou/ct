package com.example.ct


import android.content.Context

//TODO this class should actually check if the calendarEmptyTrigger is triggered, and then pass the fitting message back to the TriggerManager, which in turn passes the not to the NotificationManager
class CalendarEmptyTrigger {
    private var listener: TriggerListener? = null

    fun setTriggerListener(listener: TriggerListener) {
        this.listener = listener
    }

    fun checkCalendarEmpty() {
        // TODO: Implement the logic to check if the calendar is empty
        val calendarEmpty = true // Replace this with the actual check

        if (calendarEmpty) {
            trigger()
        }
    }

    private fun trigger() {
        listener?.onTrigger("It's time to take a walk!")
    }
}
