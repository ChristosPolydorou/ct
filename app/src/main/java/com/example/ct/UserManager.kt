package com.example.ct

import kotlin.math.abs

//TODO this class is responsible for checking if the user is signal, facilitator, or spark. we can do this (lazily) by asking them, or checking some context variables (e.g. walking speed). it then modifies the corresponding variables in the trigger manager. i would just start off with setting signal to true, the other two to false.
class UserManager (user: User) {
    //TODO modify this class to check if what type of user the user is
    // Initialize user types
    var signal: Boolean = true
    var facilitator: Boolean = false
    var spark: Boolean = false

    fun checkUserType(walkingSpeed: Double) {
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
    }
}