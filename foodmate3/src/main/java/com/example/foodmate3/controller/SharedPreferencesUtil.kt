package com.example.foodmate3.controller

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {
    private const val PREF_NAME = "SessionPrefs"
    private const val KEY_SESSION_ID = "sessionId"
    private const val KEY_SESSION_PW = "sessionPw"
    private const val KEY_SESSION_NICKNAME = "sessionNickname"
    private const val KEY_LOGGED_IN = "isLoggedIn"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, sessionId: String, sessionPw: String, sessionNickname: String?) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().apply {
            putString(KEY_SESSION_ID, sessionId)
            putString(KEY_SESSION_PW, sessionPw)
            sessionNickname?.let { putString(KEY_SESSION_NICKNAME, it) }
            putBoolean(KEY_LOGGED_IN, true) // 로그인 상태를 true로 설정
            apply()
        }
    }



    fun getSessionNickname(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(KEY_SESSION_NICKNAME, null)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().apply {
            putBoolean(KEY_LOGGED_IN, isLoggedIn)
            apply()
        }
    }

    fun checkLoggedIn(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setSessionNickname(context: Context, nickname: String) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().apply {
            putString(KEY_SESSION_NICKNAME, nickname)
            apply()
        }
    }
}