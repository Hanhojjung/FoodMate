package com.example.foodmate2.controller.controller

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val PREF_NAME = "SessionPrefs"
    private const val KEY_SESSION_ID = "sessionId"
    private const val KEY_LOGGED_IN = "isLoggedIn"

    fun saveSessionId(context: Context, sessionId: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SESSION_ID, sessionId)
        editor.apply()
    }

    fun getSessionId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sessionId = sharedPreferences.getString(KEY_SESSION_ID, null)
        if (!sessionId.isNullOrEmpty()) {
            return sessionId
        }
        return null
    }


    fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_SESSION_ID)
        editor.apply()
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun checkLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }
    fun isLoggedIn(context: Context): Boolean {
        return checkLoggedIn(context)
    }


}
