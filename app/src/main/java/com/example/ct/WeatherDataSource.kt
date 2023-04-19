package com.example.ct

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
// The listener is set in Cache class
//interface WeatherDataListener {
//    fun onWeatherDataReceived(weatherData: String)
//}

//TODO this class should be responsible for checking the weather, and then passing the info to the datasourcemanager
class WeatherDataSource(private val cache: Cache, @Transient private val context: Context) :
    DataSourceManager(cache, context), Serializable {
    //    private var listener: WeatherDataListener? = null
//    fun setWeatherDataListener(listener: WeatherDataListener) {
//        this.listener = listener
//    }
    // Loading weather data
    override fun loadData(){
        val apiUrl =
            "http://api.weatherapi.com/v1/current.json?key=affd127b42314bc3b60220131233003&q=Glasgow"
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
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

            var weatherIsGood = temperature > 15 && temperature < 25 && !isRaining && windSpeed < 10
            setCache(weatherIsGood)
        }

    }

    override fun setCache(weatherIsGood : Any) {
            cache.set(R.string.weather_is_good.toString(), weatherIsGood)
    }

//    fun getWeatherData(city: String) {
//        //val apiKey = "affd127b42314bc3b60220131233003"
//        val apiUrl = "http://api.weatherapi.com/v1/current.json?key=affd127b42314bc3b60220131233003&q=Glasgow"
//
////        WeatherDataTask(object : WeatherDataListener {
////            override fun onWeatherDataReceived(weatherData: String) {
////                listener?.onWeatherDataReceived(weatherData)
////            }
////        }).execute(apiUrl)
//    }

//    private class WeatherDataTask(val listener: WeatherDataListener) : AsyncTask<String, Void, String>() {
//        override fun doInBackground(vararg params: String?): String {
//            val urlString = params[0] ?: return ""
//
//            return try {
//                val url = URL(urlString)
//                val connection = url.openConnection() as HttpURLConnection
//                val inputStream = InputStreamReader(connection.inputStream)
//                val bufferedReader = BufferedReader(inputStream)
//                val stringBuilder = StringBuilder()
//                var line: String?
//
//                while (bufferedReader.readLine().also { line = it } != null) {
//                    stringBuilder.append(line)
//                }
//
//                connection.disconnect()
//                stringBuilder.toString()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                ""
//            }
//        }
//
//        override fun onPostExecute(result: String) {
//            listener.onWeatherDataReceived(result)
//        }
//    }


}

//class DataSourceManager {
//    private val weatherDataSource = WeatherDataSource()
//
//    init {
//        weatherDataSource.setWeatherDataListener(object : WeatherDataListener {
//            override fun onWeatherDataReceived(weatherData: String) {
//                // Process the received weather data
//            }
//        })
//    }
//    /*fun getWeatherDataForCity(city: String) {
//        weatherDataSource.getWeatherData(city)
//    }*/
//}
