package com.example.ct

import android.provider.ContactsContract
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalTime
import java.util.*

//TODO this class actually does the checking of the context, e.g. the connection to the internet and check for the weather. if the weather is good, it modifies the variables in the cache
class DatasourceManager(private val cache: Cache) {

    // Get weather data. If the weather is good, modify the variable in the cache
    fun loadWeatherData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://api.openweathermap.org/data/2.5/weather?q=LosAngeles,us&appid=" + OPEN_WEATHER_MAP_API_KEY)
            .build()
        val weatherTimer = Timer()
        weatherTimer.schedule(object : TimerTask() {
            override fun run() {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: okhttp3.Call, response: Response) {
                        try {
                            val responseBody = response.body
                            val json = JSONObject(responseBody?.string())
                            val temperature: Double = json.getJSONObject("main").getDouble("temp")
                            val isSunny = json.getJSONArray("weather").getJSONObject(0)
                                .getString("main") == "Clear"
                            var weatherIsGood = temperature > 20 && isSunny
                            // Change the variable in Cache
                            cache.set("weatherGood", weatherIsGood)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                })
            }
        }, 0, WEATHER_UPDATE_INTERVAL.toLong())
    }

    fun saveData(data: List<ContactsContract.Contacts.Data>) {
        // Implement your logic to save data.
    }

    companion object {
        private const val OPEN_WEATHER_MAP_API_KEY = "YOUR_API_KEY_HERE"
        private const val WEATHER_UPDATE_INTERVAL = 5 * 60 * 1000 // 5 minutes
    }
}