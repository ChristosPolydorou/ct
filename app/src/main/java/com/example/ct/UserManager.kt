package com.example.ct
//TODO this class is responsible for checking if the user is signal, facilitator, or spark. we can do this (lazily) by asking them, or checking some context variables (e.g. walking speed). it then modifies the corresponding variables in the trigger manager. i would just start off with setting signal to true, the other two to false.
class UserManager {
    fun getUser() {
        // Implement your logic to get the user.
    }

    fun saveUser(user: User) {
        // Implement your logic to save the user.
    }
}