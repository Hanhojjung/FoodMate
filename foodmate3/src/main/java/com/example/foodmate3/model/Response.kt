package com.example.foodmate3.model

data class LoginResponse(
    val status: String,
    val message: String,
    val sessionId: String,
    val sessionPw: String,
    val sessionNickname: String,
    val sessionImage: String
)

data class RegisterResponse(val status: String, val message: String)

data class UpdateResponse(
    val success: Boolean,
    val message: String
)
