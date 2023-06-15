package com.example.foodmate3.controller

import com.example.foodmate3.model.BarDto
import retrofit2.Call
import retrofit2.http.GET

interface BarController {
    @GET("/bars")
    fun getAllBars(): Call<List<BarDto>>
}