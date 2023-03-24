package com.example.ct

//TODO this class stores boolean variables for different situations, e.g. weatherGood, FivePm etc., the values for these are changed in the datasource manager
class Cache {
    private val cacheMap = mutableMapOf<String, Any>()

    fun <T> get(key: String): T? {
        @Suppress("UNCHECKED_CAST")
        return cacheMap[key] as? T
    }

    fun <T : Any> put(key: String, value: T) {
        cacheMap[key] = value
    }

    fun clear() {
        cacheMap.clear()
    }
}