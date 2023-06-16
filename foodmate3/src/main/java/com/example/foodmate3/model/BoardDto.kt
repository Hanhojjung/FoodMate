package com.example.foodmate3.model

data class BoardDto(
    val boardid : String,
    val userNicname : String,
    val title: String,
    val content: String,
    val barName: String,
    val barImg : String,
    val memberCount: String,
    val meetdate : String,
    val regdate : String
)