package com.example.foodmate3.controller

import com.example.foodmate3.model.TodoDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TodoController {

    @POST("/insertTodo")
    fun insertTodo(@Body todo: TodoDto): Call<ResponseBody>

    @GET("/todoList")
    fun getAllTodo(): Call<List<TodoDto>>
}