package com.example.ct

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface WeatherDataListener {
    fun onWeatherDataReceived(weatherData: String)
}

//TODO this class should be responsible for checking the weather, and then passing the info to the datasourcemanager
class WeatherDataSource {
    private var listener: WeatherDataListener? = null

    fun setWeatherDataListener(listener: WeatherDataListener) {
        this.listener = listener
    }

    fun getWeatherData(city: String) {
        //val apiKey = "affd127b42314bc3b60220131233003"
        val apiUrl = "http://api.weatherapi.com/v1/current.json?key=affd127b42314bc3b60220131233003&q=Glasgow"

        WeatherDataTask(object : WeatherDataListener {
            override fun onWeatherDataReceived(weatherData: String) {
                listener?.onWeatherDataReceived(weatherData)
            }
        }).execute(apiUrl)
    }

    private class WeatherDataTask(val listener: WeatherDataListener) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val urlString = params[0] ?: return ""

            return try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = InputStreamReader(connection.inputStream)
                val bufferedReader = BufferedReader(inputStream)
                val stringBuilder = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }

                connection.disconnect()
                stringBuilder.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        override fun onPostExecute(result: String) {
            listener.onWeatherDataReceived(result)
        }
    }
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
