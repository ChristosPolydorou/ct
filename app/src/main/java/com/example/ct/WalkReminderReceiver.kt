package com.example.ct

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.Serializable


class WalkReminderReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {

        fun <T : Serializable> getSerializable(name: String, clazz: Class<T>): Serializable
        {
//            Log.d("Build.VERSION.SDK_INT: ", Build.VERSION.SDK_INT.toString())
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getSerializableExtra(name, clazz)!!
            else
                @Suppress("DEPRECATION")
                intent.getSerializableExtra(name)!!

        }

        val weatherData = getSerializable("weather", WeatherDataSource::class.java) as WeatherDataSource
        val calendarData = getSerializable("calendar", CalendarDataSource::class.java) as CalendarDataSource
        val locationData = getSerializable("location", GeolocationDataSource::class.java) as GeolocationDataSource
        if (intent.action == "Action_For_Load_Data") {
//            Cache.put("test", System.currentTimeMillis()) // for testing
            weatherData!!.loadData(context)
            calendarData!!.loadData(context)
            locationData!!.loadData(context)
        } else if (intent.action == "Action_For_Five_Pm") {
            Cache!!.put(R.string.five_pm.toString(), true)
        }

    }

}
