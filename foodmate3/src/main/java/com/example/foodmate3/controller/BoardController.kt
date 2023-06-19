package com.example.foodmate3.controller

import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.model.MemberDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BoardController {

    @POST("/insertBoard")
    fun insertBoard(@Body board: BoardDto): Call<ResponseBody>

    @GET("/boardList")
    fun getAllBoard(): Call<List<BoardDto>>

    @GET("/getBoardDetail")
    fun getBoardDetail(
        @Query("boardid") id: String): Call<BoardDto>

//    @POST("/updateBoard/{boardId}")
//    fun updateBoard(
//        @Path("boardId") boardId: String,
//        @Query("writer") writer: String,
//        @Query("title") title: String,
//        @Query("content") content: String,
//        @Query("bar_name") barName: String,
//        @Query("user_count") userCount: String,
//        @Query("user_meetdate") userMeetDate: String
//    )


    @POST("/updateBoard")
    fun updateBoard(
        @Query("boardid") boardId: String, // 수정된 부분: boardid를 boardId로 변경
        @Body board: BoardDto // 수정된 부분: @Query 어노테이션 대신 @Body 어노테이션 사용
    )


    @GET("/myBoard")
    fun getMyBoard(@Query("userNicname") userNicname: String): Call<List<BoardDto>>

    @GET("/deleteBoard")
    fun deleteBoard(
        @Query("boardid") boardid: String
    ): Call<ResponseBody>

}