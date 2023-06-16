package com.example.foodmate3.controller

import com.example.foodmate3.model.LoginResponse
import com.example.foodmate3.model.MemberDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberController {

    @POST("/insertMember")
    fun insertMember(
        @Query("id") id: String,
        @Query("pw") pw: String,
        @Query("nickname") nickname: String
    ): Call<ResponseBody>

    @GET("/getMemberDetail")
    fun getMemberDetail(@Query("id") id: String): Call<MemberDto>

    @POST("/updateMember")
    fun updateMember(@Body member: MemberDto): Call<String>

    @GET("/deleteMember")
    fun deleteMember(@Query("id") id: String): Call<String>

    @POST("/login")
    fun login(
        @Body member: MemberDto
    ): Call<LoginResponse>

}