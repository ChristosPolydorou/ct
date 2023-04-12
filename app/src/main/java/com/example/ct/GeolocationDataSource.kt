package com.example.ct

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GeolocationDataSource(private val cache: Cache, private val activity: Activity,
                            private val context: Context) :
    DataSourceManager(cache, context) {

    private val LOCATION_HOME = "location_home"
    private val LOCATION_NEAR_JOGGING_TRACK = "location_near_jogging_track"
    private val LOCATION_GET_OFF_BUS = "location_get_off_bus"

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun loadData() {
        // Check location permissions
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            // Permission not granted, can't continue
            return
        }

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


    private fun checkLocationCriteria(location: android.location.Location) {
        val homeLocation = android.location.Location("").apply {
            latitude =  37.7749/* add latitude of user's home location */
                longitude = -122.4194/* add longitude of user's home location */
        }
        val joggingTrackLocation = android.location.Location("").apply {
            latitude = 37.7694/* add latitude of nearest jogging track */
                longitude =-122.4768/* add longitude of nearest jogging track */
        }
        val busStopLocation = android.location.Location("").apply {
            latitude = 37.7833/* add latitude of user's bus stop */
                longitude =-122.4214 /* add longitude of user's bus stop */
        }

        val isNearHome = location.distanceTo(homeLocation) < 500 // Distance in meters
        val isNearJoggingTrack = location.distanceTo(joggingTrackLocation) < 1000 // Distance in meters
        val isNearBusStop = location.distanceTo(busStopLocation) < 50 // Distance in meters

        // Prompt user to take a walk based on location criteria
        if (isNearHome || isNearJoggingTrack) {
            // Suggest taking a walk
            // ...
        } else if (isNearBusStop) {
            // Suggest getting off bus and walking to home
            // ...
        }

        // Save results to cache
        cache.set(LOCATION_HOME, isNearHome)
        cache.set(LOCATION_NEAR_JOGGING_TRACK, isNearJoggingTrack)
        cache.set(LOCATION_GET_OFF_BUS, isNearBusStop)
    }

    override fun setCache(locationResult : Any) {
        // Cache already set in checkLocationCriteria method
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
    }
}
