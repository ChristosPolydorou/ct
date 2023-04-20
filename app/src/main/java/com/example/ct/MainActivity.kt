package com.example.ct

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.os.Build
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*
import javax.sql.DataSource

private const val PERMISSIONS_REQUEST_CODE = 123

class MainActivity : AppCompatActivity() {
    private lateinit var musicBrowser: MusicBrowser

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val checkPermission = hasPermissions(this, permissions)
        if (checkPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
        }

    }


    override fun onDestroy() {
            super.onDestroy()
//            musicBrowser.disconnect()
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

    return permissions
}
