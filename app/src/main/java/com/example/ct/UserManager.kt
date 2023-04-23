package com.example.ct

import kotlin.math.abs
import android.content.Context
import android.content.SharedPreferences

//TODO this class is responsible for checking if the user is signal, facilitator, or spark. we can do this (lazily) by asking them, or checking some context variables (e.g. walking speed). it then modifies the corresponding variables in the trigger manager. i would just start off with setting signal to true, the other two to false.
class UserManager (user: User, private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = sharedPreferences.edit()
    //TODO modify this class to check if what type of user the user is
    // Initialize user types
    var userType: UserType = UserType.UNKNOWN
    /*var signal: Boolean = true
    var facilitator: Boolean = false
    var spark: Boolean = false*/

    companion object {
        private const val PREFERENCES_NAME = "user_preferences"
        private const val ROCK_MUSIC_NOTIFICATIONS_ENABLED = "rock_music_notifications_enabled"
    }

    fun checkUserType(walkingSpeed: Double) {
        userType = when {
            abs(walkingSpeed) < 0.5 -> UserType.SIGNAL
            abs(walkingSpeed) < 1.5 -> UserType.FACILITATOR
            else -> UserType.SPARK
        }
        editor.putString(R.string.user.toString(), userType.type).apply()
    }
    /*fun checkUserType(walkingSpeed: Double) {
        if (abs(walkingSpeed) < 0.5) {
            signal = true
            facilitator = false
            spark = false
        } else if (abs(walkingSpeed) < 1.5) {
            signal = false
            facilitator = true
            spark = false
        } else {
            signal = false
            facilitator = false
            spark = true
        }
    }*/

    fun shouldSendRockMusicNotification() {
        editor.putBoolean(ROCK_MUSIC_NOTIFICATIONS_ENABLED, true).apply()
//        return sharedPreferences.getBoolean(ROCK_MUSIC_NOTIFICATIONS_ENABLED, true)
    }

    fun setRockMusicNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(ROCK_MUSIC_NOTIFICATIONS_ENABLED, enabled).apply()
    }
}