package com.example.foodmate2.controller.controller

import com.google.gson.annotations.SerializedName

data class Member(
    val id: String,
    val pw: String,
    val nickname: String = "?"
)