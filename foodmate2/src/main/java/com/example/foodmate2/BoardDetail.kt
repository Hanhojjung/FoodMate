package com.example.foodmate2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BoardDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val regList: Button = findViewById(R.id.reg_list)

        regList.setOnClickListener {
            // 등록하기 버튼 클릭 시 처리할 로직 작성
            // 예: BoardList로 이동
            val intent = Intent(this@BoardDetail, MainActivity::class.java)
            startActivity(intent)
        }
    }
}