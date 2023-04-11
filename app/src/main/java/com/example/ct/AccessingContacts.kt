package com.example.ct

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AccessingContacts : AppCompatActivity() {
    companion object {
        const val PERMISSIONS_REEQUEST_READ_CONTACTS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //check if the app has permission to read contacts
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
        // Request permission to read contacts
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                                           PERMISSIONS_REEQUEST_READ_CONTACTS)
        } else {
            // Permission to read contacts has already been granted
            showContacts()
        }
    }

    @SuppressLint("Range")
    private fun showContacts() {
        // Query the Contacts content provider
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null,null,null, null )

        //Iterate over the contacts and print their names
        if (cursor != null && cursor.moveToFirst()) {
        do {
            val name = cursor.getString(
                cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME)
            )
            println("Contact name: $name")
        } while (cursor.moveToNext())
        }
    cursor?.close()
    }
    override fun onRequestPermissionResult(requestCode: Int, permission: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults)
        when (requestCode) {
            PERMISSIONS_REEQUEST_READ_CONTACTS -> {
                // If the request is cancelled, the result arrays are empty
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted
                    showContacts()
                } else {
                      // Permission denied
                }
                return
            }
        }
    }
}