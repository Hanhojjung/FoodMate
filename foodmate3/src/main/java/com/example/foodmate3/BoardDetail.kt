package com.example.foodmate3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class BoardDetail : AppCompatActivity() {
    private val titleTextView: TextView? = null
    private val dateTextView: TextView? = null
    private val nicknameTextView: TextView? = null
    private val partyoneTextView: TextView? = null
    private val appointmentTextView: TextView? = null
    private val restaurantTextView: TextView? = null
    private val contentTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val regList: Button = findViewById(R.id.reg_list)
        val regUpdate: Button = findViewById(R.id.reg_update)
        val regDelete: Button = findViewById(R.id.reg_delete)

        regList.setOnClickListener {
            // 목록 버튼으로 클릭 시 MainActivity로 이동
            val intent = Intent(this@BoardDetail, MainActivity::class.java)
            startActivity(intent)
        }

        regUpdate.setOnClickListener {
            // 예: 수정 버튼을 클릭 시 BoardUpdate로 이동
            val intent = Intent(this@BoardDetail, BoardUpdate::class.java)
            startActivity(intent)
        }

        regDelete.setOnClickListener {
            // 예: 수정 버튼을 클릭 시 BoardDelete로 이동
            val intent = Intent(this@BoardDetail, BoardDelete::class.java)
            startActivity(intent)
        }
    }
}