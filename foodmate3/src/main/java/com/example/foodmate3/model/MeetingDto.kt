package com.example.foodmate3.model

import android.os.Message
import java.lang.reflect.Member

data class MeetingDto(
    val meeting_id: String,
    val meeting_title: String,
    val meeting_content: String,
    var user: List<Member>,
    val date: String,
    val messages: List<Message>
)
