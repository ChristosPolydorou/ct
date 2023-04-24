package com.example.ct

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import com.google.android.gms.location.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager


private const val PERMISSIONS_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    private lateinit var musicBrowser: MusicBrowser


    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val speed = location.speed // Speed in meters/second

                // Create a User object (assuming you have a User class in your project)
                val user = User(type = UserType.UNKNOWN) // Replace with the appropriate constructor for your User class

                // Create an instance of UserManager and update the user type based on the walking speed
                val userManager = UserManager(user = user, context = this@MainActivity)
                userManager.checkUserType(walkingSpeed = speed.toDouble())

                // Use the speed value here, e.g., update the user type
                // val userManager = UserManager()
                // userManager.checkUserType(walkingSpeed = speed.toDouble())
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        )
        val checkPermission = hasPermissions(this, permissions)
        if (checkPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
        }

        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            startLocationUpdates()
        }
    }


    override fun onDestroy() {
            super.onDestroy()
//            musicBrowser.disconnect()
        }

    private fun startLocationUpdates() {
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
        }
    }
    }

    private fun hasPermissions(mainActivity: MainActivity, permissions: Array<String>): Array<String> {
            if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
                permissions.toMutableList().removeAt(1)
        }
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.READ_CALENDAR)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Request the permission
            permissions.toMutableList().removeAt(0)
        }
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Request the permission
            permissions.toMutableList().removeAt(2)
        }

        return permissions
    }



