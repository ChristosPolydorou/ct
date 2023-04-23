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

//        val cache = getSerializable("cache", Cache::class.java) as Cache
        val weatherData = getSerializable("weather", WeatherDataSource::class.java) as WeatherDataSource
//        val calendarData = intent.getSerializableExtra("calendar", CalendarDataSource::class.java)
//        val locationData = intent.getSerializableExtra("location", GeolocationDataSource::class.java)
        if (intent.action == "Action_For_Load_Data") {
//            cache.checkNull(context)
            Cache.set(R.string.steps.toString(), System.currentTimeMillis())
            weatherData!!.loadData(context)
//            calendarData!!.loadData()
//            locationData!!.loadData()
        } else if (intent.action == "Action_For_Five_Pm") {
//            cache.checkNull(context)
//            cache!!.set(R.string.five_pm.toString(), true)
        }

    }

}
