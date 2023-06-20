package com.example.foodmate3.controller

import com.example.foodmate3.model.MeetingDto
import com.example.foodmate3.model.MemberDto
import com.example.foodmate3.model.MessageDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MeetingController {

    @GET("/getMeetingByNickname")
    fun getMeetingByNickname(
        @Query("nickname") nickname: String
    ): Call<ResponseBody>

    @POST("/insertMeeting")
    fun insertMeeting(@Body meeting: MeetingDto): Call<ResponseBody>

    @GET("/getOneMeeting")
    fun getOneMeeting(@Query("meeting_id") meeting_id: String): Call<MeetingDto>

    @GET("/deleteMeeting")
    fun deleteMeeting(@Query("meeting_id") meeting_id: String): Call<MeetingDto>

    @POST("/updateMeeting")
    fun updateMeeting(
        @Query("meeting_id") meeting_id: String,
        @Body meeting: MeetingDto
    ): Call<ResponseBody>

    @POST("/addMember")
    fun addMember(
        @Query("meeting_id") meeting_id: String,
        @Body member: MemberDto
    ): Call<ResponseBody>

    @POST("/removeMember")
    fun removeMember(
        @Query("meeting_id") meeting_id: String,
        @Body member: MemberDto
    ): Call<ResponseBody>

    @POST("/addMessage")
    fun addMessage(
        @Query("meeting_id") meeting_id: String,
        @Body message: MessageDto
    ): Call<ResponseBody>

}