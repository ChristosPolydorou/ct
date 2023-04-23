package com.example.ct

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.util.Log
import java.io.Serializable


//TODO this class stores boolean variables for different situations, e.g. weatherGood, FivePm etc., the values for these are changed in the datasource manager
object Cache {

    private lateinit var situationsCache: SharedPreferences
    private lateinit var  triggerManager: TriggerManager
    fun initializeCache(context: Context){
         situationsCache = context.getSharedPreferences(
            R.string.situations_cache.toString(), MODE_PRIVATE)
           triggerManager = TriggerManager(context)

        // Uniform key names are set in the strings.xml
        // If it's the first time open the sharedPreferences file
        if (!situationsCache.contains("is_first_time")){
            put("is_first_time", true)
            put(R.string.weather_is_good.toString(), false)
            put(R.string.location_near.toString(), false)
            put(R.string.five_pm.toString(), false)
            put(R.string.calendar_is_empty.toString(), false)
            put(R.string.steps.toString(), 0L)
            put(R.string.target.toString(), 0)
        }
        // Register a listener to watch every variables.
         situationsCache.registerOnSharedPreferenceChangeListener { _, key ->
             triggerManager.checkTriggers(key, get(key))
         }
    }

    fun get(key: String): Any {
//        return cacheMap[key] as T
        return if(key==R.string.steps.toString() || key==R.string.target.toString()){
            situationsCache.getInt(key, -1)
        }else{
            situationsCache.getBoolean(key, false)
        }

    }

    private fun  put(key: String, value: Any) {
//        cacheMap[key] = value
        when(value){
            is Int -> {
                situationsCache.edit().putInt(key, value)
                situationsCache.edit().apply()
            }
            is Boolean -> {
                situationsCache.edit().putBoolean(key, value)
                situationsCache.edit().apply()
            }
            is Long -> {
                situationsCache.edit().putLong(key, value)
                situationsCache.edit().apply()
            }
        }

    }

    fun  set(key: String, value: Any) {
//        cacheMap[key] = value
        put(key, value)
        situationsCache.edit().apply()
    }

//     fun checkNull(context: Context){
//        if(this.context==null){
//            this.context = context
//        }
//        if (situationsCache == null){
//            situationsCache = context.getSharedPreferences(R.string.situations_cache.toString(), MODE_PRIVATE)
//            editor = situationsCache.edit()
//        }
//         if (triggerManager == null){
//             triggerManager = TriggerManager(context)
//         }
//         // Register a listener to watch every variables.
//         situationsCache.registerOnSharedPreferenceChangeListener { _, key ->
//             triggerManager.checkTriggers(key, get(key))
//         }
//     }

    fun clear() {
        situationsCache.edit().clear()
    }
}