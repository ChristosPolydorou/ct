package com.example.ct

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.util.Log


//TODO this class stores boolean variables for different situations, e.g. weatherGood, FivePm etc., the values for these are changed in the datasource manager
object Cache {

    private lateinit var situationsCache: SharedPreferences
    private lateinit var  triggerManager: TriggerManager
    private lateinit var listener: OnSharedPreferenceChangeListener
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
            put(R.string.steps.toString(), 0)
            put(R.string.target.toString(), 0)
        }
        // Register a listener to watch every variables
        listener = OnSharedPreferenceChangeListener { situationsCache, key ->
            triggerManager.checkTriggers(key, get(key))
        }
         situationsCache.registerOnSharedPreferenceChangeListener (listener)
    }

    fun get(key: String): Any {
//        return cacheMap[key] as T
        return if(key==R.string.steps.toString() || key==R.string.target.toString()){
            situationsCache.getInt(key, -1)
        }else if(key=="test"){
            situationsCache.getLong(key, 0L)
        } else{
            situationsCache.getBoolean(key, false)
        }

    }

     fun  put(key: String, value: Any) {
//        cacheMap[key] = value
        when(value){
            is Int -> {
                situationsCache.edit().putInt(key, value).apply()
            }
            is Boolean -> {
                situationsCache.edit().putBoolean(key, value).apply()
            }
            is Long -> {
                situationsCache.edit().putLong(key, value).apply()
            }
        }
//         Log.d("test", situationsCache.getLong("test", 0L).toString())
//         Log.d(R.string.weather_is_good.toString(), situationsCache.contains(R.string.weather_is_good.toString()).toString())

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
        situationsCache.unregisterOnSharedPreferenceChangeListener(listener)
    }
}