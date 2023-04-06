package com.example.ct

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.provider.CalendarContract
import java.util.*

//TODO this class should actually do the checking of the calendar


//interface CalendarDataListener {
//    fun onCalendarDataReceived(events: List<CalendarEvent>)
//}
class CalendarDataSource(private val cache: Cache, private val context: Context) : DataSourceManager(cache, context){
//    private var listener: CalendarDataListener? = null

//    fun setCalendarDataListener(listener: CalendarDataListener) {
//        this.listener = listener
//    }

    @SuppressLint("Range")
    fun getCalendarEvents() {
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

//        listener?.onCalendarDataReceived(events)
    }

    @SuppressLint("Range")
    fun getCalendarEvent(eventId: Long): CalendarEvent? {
        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        return cursor?.use {
            if (it.moveToFirst()) {
                val title = it.getString(it.getColumnIndex(CalendarContract.Events.TITLE))
                val description = it.getString(it.getColumnIndex(CalendarContract.Events.DESCRIPTION))
                val start = it.getLong(it.getColumnIndex(CalendarContract.Events.DTSTART))
                val end = it.getLong(it.getColumnIndex(CalendarContract.Events.DTEND))

                CalendarEvent(
                    eventId,
                    title,
                    description,
                    Date(start),
                    Date(end)
                )
            } else {
                null
            }
        }
    }

    override fun loadData() {
        setCache()
    }

    override fun setCache() {
        cache.set(R.string.calendar_is_empty.toString(), getCalendarEvents())
    }
}

data class CalendarEvent(
    val eventId: Long,
    val title: String?,
    val description: String?,
    val startDate: Date?,
    val endDate: Date?
)