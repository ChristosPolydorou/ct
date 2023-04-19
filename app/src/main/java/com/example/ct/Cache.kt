package com.example.ct

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import java.io.Serializable


//TODO this class stores boolean variables for different situations, e.g. weatherGood, FivePm etc., the values for these are changed in the datasource manager
class Cache (@Transient private val context: Context, @Transient private val triggerManager: TriggerManager) : Serializable {
    var situationsCache: SharedPreferences = context.getSharedPreferences(
        R.string.situations_cache.toString(), MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = situationsCache.edit()
//    private val cacheMap = mutableMapOf<String, Any>()

    init {
        // Uniform key names are set in the strings.xml
//        cacheMap["weatherGood"] = false
//        cacheMap["FivePm"] = false
//        cacheMap["steps"] = 0
//        cacheMap["target"] = 0
//        cacheMap["calendarEmpty"] = false

        // Uniform key names are set in the strings.xml
        // If it's the first time open the sharedPreferences file
        if (!situationsCache.contains("is_first_time")){
            put("is_first_time", true)
            put(R.string.weather_is_good.toString(), false)
            put("location_near".toString(), false)
            put(R.string.five_pm.toString(), false)
            put(R.string.calendar_is_empty.toString(), false)
            put(R.string.steps.toString(), 0)
            put(R.string.target.toString(), 0)

            // Register a listener to watch every variables.
            situationsCache.registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener {
                    cachePref, key ->
                triggerManager.checkTriggers(key, get(key))
            })
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
                editor.putInt(key, value)
                editor.apply()
            }
            is Boolean -> {
                editor.putBoolean(key, value)
                editor.apply()
            }
        }

    }

    fun  set(key: String, value: Any) {
//        cacheMap[key] = value
        put(key, value)
        editor.apply()
    }

    fun clear() {
        editor.clear()
    }
}