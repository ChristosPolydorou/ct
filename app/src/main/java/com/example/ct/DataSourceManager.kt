package com.example.ct

import android.content.ContentUris
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.text.format.DateUtils
import java.io.Serializable
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import java.util.*


//TODO this class formats how to manage data source. It is derived by WeatherDataSource and CalendarDataSource
abstract class DataSourceManager :
    Serializable {
    abstract fun loadData(context: Context)
    abstract fun setCache(cacheData:Any)


}