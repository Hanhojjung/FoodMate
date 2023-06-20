package com.example.foodmate3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmate3.adapter.ChatAdapter
import com.example.foodmate3.controller.MeetingController
import com.example.foodmate3.databinding.ActivityMeetingBinding
import com.example.foodmate3.model.MeetingDto
import com.example.foodmate3.model.MessageDto
import com.example.foodmate3.network.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class MeetingActivity : AppCompatActivity() {

    private lateinit var meetingDto: MeetingDto
    val TAG: String = "MeetingActivity"
    private lateinit var binding: ActivityMeetingBinding
    private lateinit var meetingService: MeetingController

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meetingService = RetrofitBuilder.MeetingService()
        binding.chatListRecyclerView.adapter = chatAdapter

//        binding.updateButton.setOnClickListener {
//            val updatedMeeting = // 업데이트할 MeetingDto 객체 생성
//            val meetingId = meetingDto.meeting_id
//            updateMeeting(meetingId, updatedMeeting)
//        }
//
//        binding.deleteButton.setOnClickListener {
//            val meetingId = meetingDto.meeting_id
//            deleteMeeting(meetingId)
//        }

//        binding.loadMessagesButton.setOnClickListener {
//            val meetingId = meetingDto.meeting_id
//            loadMessages(meetingId)
//        }

        binding.chatSendBtn.setOnClickListener {
            val meetingId = meetingDto.meeting_id
            val messageContent = binding.chatMsgEdit.text.toString()
            sendMessage(meetingId, messageContent)
        }
    }

    private fun updateMeeting(meetingId: String, updatedMeeting: MeetingDto) {
        val call = meetingService.updateMeeting(meetingId, updatedMeeting)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 업데이트 성공
                    // TODO: 업데이트 완료 후 동작 처리
                } else {
                    // 업데이트 실패
                    Toast.makeText(this@MeetingActivity, "Failed to update meeting", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@MeetingActivity, "Failed to update meeting", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteMeeting(meetingId: String) {
        val call = meetingService.deleteMeeting(meetingId)
        call.enqueue(object : Callback<MeetingDto> {
            override fun onResponse(call: Call<MeetingDto>, response: Response<MeetingDto>) {
                if (response.isSuccessful) {
                    // 삭제 성공
                    // TODO: 삭제 완료 후 동작 처리
                } else {
                    // 삭제 실패
                    Toast.makeText(this@MeetingActivity, "Failed to delete meeting", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MeetingDto>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@MeetingActivity, "Failed to delete meeting", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadMessages(meetingId: String) {
        val call = meetingService.getOneMeeting(meetingId)
        call.enqueue(object : Callback<MeetingDto> {
            override fun onResponse(call: Call<MeetingDto>, response: Response<MeetingDto>) {
                if (response.isSuccessful) {
                    val meeting = response.body()
                    if (meeting != null) {
                        val messages = meeting.messages
                        chatAdapter.submitList(messages)
                        chatAdapter.notifyDataSetChanged()
                    }
                } else {
                    // 메시지 불러오기 실패
                    Toast.makeText(this@MeetingActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MeetingDto>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@MeetingActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(meetingId: String, messageContent: String) {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")//한국 날짜 시간 변경
        val curTime = dateFormat.format(Date(time)).toString()
        val nickname = nickname
        val message = MessageDto(nickname, messageContent, curTime)
        val call = meetingService.addMessage(meetingId, message)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 메시지 보내기 성공
                    // TODO: 메시지 보내기 완료 후 동작 처리
                } else {
                    // 메시지 보내기 실패
                    Toast.makeText(
                        this@MeetingActivity,
                        "Failed to send message",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패
                Toast.makeText(this@MeetingActivity, "Failed to send message", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}