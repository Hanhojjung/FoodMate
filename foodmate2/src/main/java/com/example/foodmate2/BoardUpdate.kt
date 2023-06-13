package com.example.foodmate2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BoardUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_update)

        val regUpdate: Button = findViewById(R.id.reg_update)
        val regUpdateCancel: Button = findViewById(R.id.reg_update_cancel)

        regUpdate.setOnClickListener {
            // 예: 수정 버튼을 클릭 시 BoardUpdate로 이동
            val intent = Intent(this@BoardUpdate, BoardDetail::class.java)
            startActivity(intent)
        }

        regUpdateCancel.setOnClickListener {
            // 예: 수정 버튼을 클릭 시 BoardUpdate로 이동
            val intent = Intent(this@BoardUpdate, BoardDetail::class.java)
            startActivity(intent)
        }
    }
}