package com.example.foodmate2

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.foodmate2.controller.controller.Board
private lateinit var board: Board
class BoardDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        board = intent.getSerializableExtra("board") as Board

        val intent = intent

        val title = intent.getStringExtra("title")
        val user_count = intent.getStringExtra("user_count")
        val bar_name = intent.getStringExtra("bar_name")
        val bar_appoint = intent.getStringExtra("bar_appoint")
        val content = intent.getStringExtra("content")

        // 데이터를 활용하여 화면에 표시하는 코드 작성

        val regList: Button = findViewById(R.id.reg_list)
        val regUpdate: Button = findViewById(R.id.reg_update)
        val regDelete: Button = findViewById(R.id.reg_delete)

        regList.setOnClickListener {
            // 목록 버튼으로 클릭 시 MainActivity로 이동
            val intent = Intent(this@BoardDetail, MainActivity::class.java)
            startActivity(intent)
        }

        regUpdate.setOnClickListener {
            // 수정 버튼을 클릭 시 BoardUpdate로 이동
            val intent = Intent(this@BoardDetail, BoardUpdate::class.java)
            startActivity(intent)
        }

        regDelete.setOnClickListener {
            // 삭제 버튼을 클릭 시 BoardDelete로 이동
            val intent = Intent(this@BoardDetail, BoardDelete::class.java)
            startActivity(intent)
        }
    }
}
