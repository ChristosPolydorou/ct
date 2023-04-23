package com.example.ct

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.Serializable


class GeolocationDataSource :
    DataSourceManager(), Serializable {

    private val LOCATION_HOME = "location_home"
    private val LOCATION_NEAR_JOGGING_TRACK = "location_near_jogging_track"
    private val LOCATION_GET_OFF_BUS = "location_get_off_bus"

    @SuppressLint("MissingPermission")
    override fun loadData(context: Context) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        // Get the user's current location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // User's location retrieved, proceed to check location criteria
                checkLocationCriteria(location)
            } else {
                // Unable to retrieve user's location, handle error
            }
        }

    }

    /*private fun checkIfBusStopNear(location: Location): Boolean{
        //val busStopLocations = mutableListOf<android.location.Location>()
        val api_key = "UjUud0ZB0huzwiXOVM6rMnOWuAVZ8m4C"
        val latitude = location?.latitude ?: 0.0
        val longitude = location?.longitude ?: 0.0
        val radius = 100
        val url = "https://transit.land/api/v1/stops?lon=$longitude&lat=$latitude&r=$radius"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("Api-Key", api_key)
            .build()

        var res = false

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonString = response.body?.string()
                val json = JSONObject(jsonString)
                val stopsArray = json.getJSONArray("stops")

                res = stopsArray.length() >= 0
            }
        })
        return res
    }*/

    private fun checkIfBusStopNear(location: Location, scope: CoroutineScope): Boolean {
        val latitude = location.latitude
        val longitude = location.longitude
        val radius = 100
        val overpassUrl =
            "https://overpass-api.de/api/interpreter?data=[out:json];(node[highway=bus_stop](around:$radius,$latitude,$longitude);way[highway=bus_stop](around:$radius,$latitude,$longitude);relation[highway=bus_stop](around:$radius,$latitude,$longitude););out;"
        var isBusStopNearby: Boolean = false
        scope.launch {
            val document = Jsoup.connect(overpassUrl).ignoreContentType(true).get()
            val busStopElements = document.select("element")
            isBusStopNearby = busStopElements.isNotEmpty()
        }
        return isBusStopNearby
    }


    private fun checkIfParkIsNear(location: Location, scope: CoroutineScope): Boolean {
        var isParkNearby = false
        val latitude = location?.latitude ?: 0.0
        val longitude = location?.longitude ?: 0.0
        val radius = 100
        val overpassUrl =
            "https://overpass-api.de/api/interpreter?data=[out:json];(node[leisure=park](around:$radius,$latitude,$longitude);way[leisure=park](around:$radius,$latitude,$longitude);relation[leisure=park](around:$radius,$latitude,$longitude););out;"
        scope.launch {
            val document = Jsoup.connect(overpassUrl).ignoreContentType(true).get()
            val parkElements = document.select("element")
            isParkNearby = parkElements.isNotEmpty()
        }

        return isParkNearby
    }


    private fun checkLocationCriteria(location: android.location.Location) {
        val scope = WalkReminderService.getScopeForLoadData()
        val homeLocation = android.location.Location("").apply {
            latitude = 37.7749/* add latitude of user's home location */
            longitude = -122.4194/* add longitude of user's home location */
        }
        /*val joggingTrackLocation = android.location.Location("").apply {
            latitude = 37.7694/* add latitude of nearest jogging track */
                longitude =-122.4768/* add longitude of nearest jogging track */
        }*/
        //val busStopLocations = checkIfBusStopNear()
        /*android.location.Location("").apply {
            latitude = 37.7833/* add latitude of user's bus stop */
                longitude =-122.4214 /* add longitude of user's bus stop */
        }*/

        val isNearHome = location.distanceTo(homeLocation) < 500 // Distance in meters
        val isNearJoggingTrack = checkIfParkIsNear(
            location,
            scope
        )//location.distanceTo(joggingTrackLocation) < 1000 // Distance in meters
        var isNearBusStop = checkIfBusStopNear(location, scope)
        /*for (item in busStopLocations){
            if(location.distanceTo(item) < 50){
                isNearBusStop = true// Distance in meters
                break
            }
        }*/

        // Prompt user to take a walk based on location criteria
        if (isNearHome || isNearJoggingTrack) {
            // Suggest taking a walk
            // ...
        } else if (isNearBusStop) {
            // Suggest getting off bus and walking to home
            // ...
        }

        // Save results to cache
        Cache.put(LOCATION_HOME, isNearHome)
        Cache.put(LOCATION_NEAR_JOGGING_TRACK, isNearJoggingTrack)
        Cache.put(LOCATION_GET_OFF_BUS, isNearBusStop)
    }

    override fun setCache(cacheData: Any) {
        // Cache already set in checkLocationCriteria method
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
    }
}
