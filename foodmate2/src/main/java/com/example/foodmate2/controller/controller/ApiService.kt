package com.example.foodmate2.controller.controller

import com.example.foodmate2.controller.controller.Member
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService  {
    @POST("test/insertMember/{point}")
    fun insertMember(
        @Path("point") point: String,
        @Query("id") id: String,
        @Query("pw") pw: String,
        @Query("nickname") nickname: String
    ): Call<ResponseBody>

    @GET("/test/getMemberDetail")
    fun getMemberDetail(@Query("id") id: String): Call<Member>

    @POST("/test/updateMember")
    fun updateMember(
        @Query("id") id: String,
        @Query("pw") pw: String,
        @Query("nickname") nickname: String
    ): Call<String>

    @GET("/test/deleteMember")
    fun deleteMember(@Query("id") id: String): Call<String>

    @POST("test/login")
    fun login(
        @Body member: Member
    ): Call<LoginResponse>

    @POST("/api/logout")
    fun logout(
        @Header("Cookie") sessionId: String
    ): Call<String>

    @GET("/api/checkSession")
    fun checkSession(
        @Header("Cookie") sessionId: String
    ): Call<String>

}
