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

    fun getSessionId(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(KEY_SESSION_ID, null)
    }

    fun getSessionPw(context: Context): String? {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString(KEY_SESSION_PW, null)
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

    fun updateSession(context: Context, id: String, password: String, nickname: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_SESSION_ID, id)
        editor.putString(KEY_SESSION_PW, password)
        editor.putString(KEY_SESSION_NICKNAME, nickname)
        editor.apply()
    }


    fun reloadSession(context: Context, sessionId: String, sessionPw: String, sessionNickname: String?) {
        val sharedPreferences = getSharedPreferences(context)
        val loadedSessionId = sharedPreferences.getString(KEY_SESSION_ID, null)

        if (loadedSessionId == sessionId) {
            saveSession(context, sessionId, sessionPw, sessionNickname)
        }
    }

    fun clearSession(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun checkSessionExists(context: Context): Boolean {
        val sharedPreferences = getSharedPreferences(context)
        val sessionId = sharedPreferences.getString(KEY_SESSION_ID, null)
        val sessionPw = sharedPreferences.getString(KEY_SESSION_PW, null)
        val sessionNickname = sharedPreferences.getString(KEY_SESSION_NICKNAME, null)

        return !sessionId.isNullOrEmpty() && !sessionPw.isNullOrEmpty() && !sessionNickname.isNullOrEmpty()
    }

    fun reloadSessionAfterWithdrawal(context: Context, sessionId: String, sessionPw: String, sessionNickname: String?) {
        val sharedPreferences = getSharedPreferences(context)
        val loadedSessionId = sharedPreferences.getString(KEY_SESSION_ID, null)

        if (loadedSessionId == sessionId) {
            val savedSessionPw = sharedPreferences.getString(KEY_SESSION_PW, null)
            val savedSessionNickname = sharedPreferences.getString(KEY_SESSION_NICKNAME, null)
            saveSession(context, sessionId, savedSessionPw ?: "", savedSessionNickname)
        }
    }



}