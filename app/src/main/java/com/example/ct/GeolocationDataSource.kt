package com.example.ct

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.content.ContextCompat
//import com.google.android.libraries.places.api.Places
//import com.mapbox.api.geocoding.v5.models.CarmenFeature
//import com.mapbox.geojson.Point


class GeolocationDataSource(private val cache: Cache, private val context: Context) :
    DataSourceManager(cache, context) {

    private var locationResult = mutableMapOf<String, Boolean>()
    private val LOCATION_NEAR = "location_near"

    @SuppressLint("MissingPermission")
    override fun loadData() {
        // TODO Get the user's current location
        val locationProvider = LocationManager.NETWORK_PROVIDER
        val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java) as LocationManager
        val location = locationManager.getLastKnownLocation(locationProvider)!!
        locationManager.getLastKnownLocation(locationProvider)

        // Set up the Mapbox Places client
//        val placesClient = Places.createClient(context)

        // Define the search parameters
//        val searchOptions = CarmenChipOptions.builder()
//            .addPlaceCategory(PlaceCategory.PARK)
//            .proximity(Point.fromLngLat(location.longitude, location.latitude))
//            .radius(10000) // Search radius in meters
//            .build()
//
//        // Search for nearby points of interest
//        placesClient.fetchPlace(searchOptions, object : CarmenCallback<expected<MutableList<CarmenFeature>, CarmenError> {
//            override fun onError(error: CarmenError) {
//                // Handle error
//            }
//
//            override fun onResult(result: expected<MutableList<CarmenFeature>>) {
//                if (result is expected.Success) {
//                    val parkNearby = result.value.isNotEmpty()
//                    locationResult[LOCATION_NEAR] = parkNearby
//                } else {
//                    locationResult[LOCATION_NEAR] = false
//                }
//
//                // Call setCache to save the result
//                setCache(locationResult[LOCATION_NEAR])
//            }
//        })

        // TODO Call setCache to save the result (if the place is user's office, home or some park)

    }


    override fun setCache(locationResult : Any) {
        cache.set(LOCATION_NEAR, locationResult)
    }

    companion object {
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    }
}

