import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ct.Cache
import com.example.ct.DataSourceManager
import com.example.ct.R
import com.google.android.libraries.places.api.Places
import com.mapbox.api.geocoding.v5.models.CarmenFeature

//import com.mapbox.mapboxsdk.plugins.places.picker.model.CarmenChipOptions
//
//import com.mapbox.api.geocoding.v5.MapboxGeocoding
//import com.mapbox.api.geocoding.v5.models.CarmenFeature
//import com.mapbox.geojson.Point
//import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
//import com.mapbox.mapboxsdk.plugins.places.common.model.PlaceCategory
//import com.mapbox.mapboxsdk.plugins.places.picker.model.PointSelectionOptions
//import com.mapbox.mapboxsdk.plugins.places.picker.model.PickerUIOptions
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PlacePicker
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PlaceSelectionListener
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PickerBottomSheetLayout
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PickerFragment
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PickerMode
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.PickerUI
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.autocomplete.PlaceAutocompleteFragment
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.autocomplete.PlaceAutocompleteViewModel
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.map.PlacePickerNavigationFragment
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.map.PlacePickerNavigationViewModel
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.map.mapbox.MapboxPlacePickerFragment
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.map.mapbox.MapboxPlacePickerViewModel
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.sheet.PlacePickerBottomSheetFragment
//import com.mapbox.mapboxsdk.plugins.places.picker.ui.sheet.PlacePickerBottomSheetViewModel
//import com.mapbox.mapboxsdk.plugins.places.picker.util.ColorUtils
//import com.mapbox.mapboxsdk.plugins.places.picker.util.MapUtils
//import com.mapbox.mapboxsdk.plugins.places.picker.util.MapboxUtils
//import com.mapbox.mapboxsdk.plugins.places.picker.util.ViewUtils



class GeolocationDataSource(private val cache: Cache, private val context: Context) :
    DataSourceManager(cache, context) {

    private var locationResult = mutableMapOf<String, Boolean>()
    private val LOCATION_NEAR = "location_near"

    @SuppressLint("MissingPermission")
    override fun loadData() {
        // Get the user's current location
        val locationProvider = LocationManager.NETWORK_PROVIDER
        val locationManager = ContextCompat.getSystemService(context, LocationManager::class.java) as LocationManager
        val location = locationManager.getLastKnownLocation(locationProvider)
        locationManager.getLastKnownLocation(locationProvider)

        // Set up the Mapbox Places client
        val placesClient = Places.createClient(context))

        // Define the search parameters
        val searchOptions = CarmenChipOptions.builder()
            .addPlaceCategory(PlaceCategory.PARK)
            .proximity(Point.fromLngLat(location.longitude, location.latitude))
            .radius(10000) // Search radius in meters
            .build()

        // Search for nearby points of interest
        placesClient.fetchPlaces(searchOptions, object : CarmenCallback<Expected<MutableList<CarmenFeature>>, CarmenError> {
            override fun onError(error: CarmenError) {
                // Handle error
            }

            override fun onResult(result: Expected<MutableList<CarmenFeature>>) {
                if (result is Expected.Success) {
                    val parkNearby = result.value.isNotEmpty()
                    locationResult[LOCATION_NEAR] = parkNearby
                } else {
                    locationResult[LOCATION_NEAR] = false
                }

                // Call setCache to save the result
                setCache()
            }
        })
    }


    override fun setCache() {
        cache.set(LOCATION_NEAR, locationResult[LOCATION_NEAR]!!)
    }

    companion object {
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    }
}

