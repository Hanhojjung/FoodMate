package com.example.foodmate3.controller

import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.model.MemberDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BoardController {

    @POST("/insertBoard")
    fun insertBoard(
        @Query("writter") writter: String,
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("bar_name") bar_name: String,
        @Query("user_count") user_count: String,
        @Query("meetdate") bar_appoint : String,
        @Query("regdate")regdate : String

    ): Call<ResponseBody>


    @GET("/getBoardDetail")
    fun getBoardDetail(
        @Query("id") id: String): Call<BoardDto>

    @POST("/updateBoard")
    fun updateBoard(
        @Query("writter") writter: String,
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("bar_name") bar_name: String,
        @Query("user_count") user_count: String,
        @Query("bar_appoint") bar_appoint : String
        )
}