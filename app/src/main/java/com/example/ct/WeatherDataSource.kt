package com.example.ct

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// The listener is set in Cache class
//interface WeatherDataListener {
//    fun onWeatherDataReceived(weatherData: String)
//}

//TODO this class should be responsible for checking the weather, and then passing the info to the datasourcemanager
class WeatherDataSource :
    DataSourceManager(), Serializable {
    //    private var listener: WeatherDataListener? = null
//    fun setWeatherDataListener(listener: WeatherDataListener) {
//        this.listener = listener
//    }

    // Loading weather data
    override fun loadData(context: Context){
        val scope = WalkReminderService.getScopeForLoadData()
        scope.launch {
            val apiUrl =
                "https://api.weatherapi.com/v1/current.json?key=affd127b42314bc3b60220131233003&q=Glasgow"
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"

            val inputStream = InputStreamReader(connection.inputStream)
            val bufferedReader = BufferedReader(inputStream)
            val stringBuilder = StringBuilder()
            var line: String?

            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            bufferedReader.close()
            inputStream.close()

            connection.disconnect()

            // TODO("Check the new weather data if the weather is good or not then update cache.")
            // Parse the JSON data
            val json = JSONObject(stringBuilder.toString())
            if(json!=null){
                val current = json.getJSONObject("current")

                // Check if the temperature, precipitation, and wind speed are good for a walk
                val temperature = current.getDouble("temp_c")
                val isRaining = current.getDouble("precip_mm") > 0
                val windSpeed = current.getDouble("wind_kph")

//                var weatherIsGood = temperature > 15 && temperature < 25 && !isRaining && windSpeed < 10
                Log.d("temperature:", temperature.toString())
                Log.d("isRaining:", isRaining.toString())
                Log.d("windSpeed:", windSpeed.toString())
                var weatherIsGood = temperature > 5 && temperature < 25  && windSpeed < 30
                setCache(weatherIsGood)
            }
        }

    }

    override fun setCache(weatherIsGood : Any) {
            Cache.put(R.string.weather_is_good.toString(), weatherIsGood)
    }

}

