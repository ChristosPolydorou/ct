package com.example.ct

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.provider.CalendarContract
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

//TODO this class should actually do the checking of the calendar

class CalendarDataSource : DataSourceManager(),
    Serializable{
    @SuppressLint("Range")
    fun getCalendarEvents(context: Context) : MutableList<CalendarEvent>{
        val currentTime = System.currentTimeMillis()
        val endTime = currentTime + 86400000 // 24 hours from now

        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?"
        val selectionArgs = arrayOf(currentTime.toString(), endTime.toString())
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)

        val events = mutableListOf<CalendarEvent>()

        cursor?.use {
            while (it.moveToNext()) {
                val eventId = it.getLong(it.getColumnIndex(CalendarContract.Events._ID))
                val title = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                val description = it.getString(it.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                val start = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                val end = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))

                val event = CalendarEvent(
                    eventId,
                    title,
                    description,
                    Date(start),
                    Date(end)
                )

                events.add(event)
            }
        }

        return events
    }


    override fun loadData(context: Context) {
        val scope = WalkReminderService.getScopeForLoadData()

        scope.launch {
            var calendarIsEmpty:Boolean = false
            val events = getCalendarEvents(context)
             calendarIsEmpty = events.isEmpty()
            setCache(calendarIsEmpty)
        }
    }

    override fun setCache(calendarIsEmpty : Any) {
        Cache.put(R.string.calendar_is_empty.toString(), calendarIsEmpty)
    }
}

data class CalendarEvent(
    var eventId: Long,
    var title: String?,
    var description: String?,
    @Transient var startDate: Date?,
   @Transient var endDate: Date?
): Serializable