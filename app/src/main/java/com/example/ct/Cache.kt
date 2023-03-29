package com.example.ct

//TODO this class stores boolean variables for different situations, e.g. weatherGood, FivePm etc., the values for these are changed in the datasource manager
class Cache {
    private val cacheMap = mutableMapOf<String, Any>()

    init {
        // Use uniform keys
        cacheMap["weatherGood"] = false
        cacheMap["FivePm"] = false
        cacheMap["steps"] = 0
        cacheMap["target"] = 0
        cacheMap["calendarEmpty"] = false
    }

    fun <T> get(key: String): T {
        @Suppress("UNCHECKED_CAST")
        return cacheMap[key] as T
    }

    fun <T : Any> put(key: String, value: T) {
        cacheMap[key] = value
    }

    fun <T : Any> set(key: String, value: T) {
        cacheMap[key] = value
    }

    fun clear() {
        cacheMap.clear()
    }
}