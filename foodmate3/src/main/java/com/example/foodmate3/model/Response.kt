package com.example.foodmate3.model

data class LoginResponse(
    val status: String,
    val message: String,
    val sessionId: String,
    val sessionPw: String,
    val nickname: String
)

data class RegisterResponse(val status: String, val message: String)
