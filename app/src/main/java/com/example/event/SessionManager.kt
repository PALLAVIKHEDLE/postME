package com.example.event
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlin.math.log

class SessionManager(context: Context) {
    //  private lateinit var databaseHelper: DatabaseHelper
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)


    // Constants for our session data keys
    companion object {
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_PROFILEPHOTO = "profilephoto"
    }

    fun createSession(email: String) {
        // Store session data using SharedPreferences
        sharedPreferences.edit()
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun createDataSession(name: String, phone: String) {
        // Store session data using SharedPreferences
//        val username= sharedPreferences.getString(KEY_NAME, null)
//        val userphone= sharedPreferences.getString(KEY_PHONE, null)
        Log.d("ALLDataTest!!", "DataSessionDetails-> $name, $phone")
        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putString(KEY_PHONE, phone)
            .putString(KEY_PROFILEPHOTO, phone)
            .apply()
    }

    fun getSessionEmail(): String? {
        // Retrieve session data using SharedPreferences
        return sharedPreferences.getString(KEY_EMAIL,null)
    }
    fun getSessionName(): String? {
        // Retrieve session data using SharedPreferences
        return sharedPreferences.getString(KEY_NAME, null)
    }
    fun getSessionPhone(): String? {
        // Retrieve session data using SharedPreferences
        return sharedPreferences.getString(KEY_PHONE, null)
    }


    fun clearSession() {
        // Clear session data using SharedPreferences
        sharedPreferences.edit().clear().apply()
    }
}
