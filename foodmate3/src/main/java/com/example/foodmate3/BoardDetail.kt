package com.example.foodmate3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ActivityBoardDetailBinding
import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardDetail : AppCompatActivity() {

    private val TAG: String = "BoardDetail"
    private lateinit var binding: ActivityBoardDetailBinding
    private lateinit var boardService: BoardController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardService = RetrofitBuilder.BoardListService()

        val regList: Button = findViewById(R.id.reg_list)
        val regUpdate: Button = findViewById(R.id.reg_update)
        val regDelete: Button = findViewById(R.id.reg_delete)

        regList.setOnClickListener {
            // 목록 버튼으로 클릭 시 MainActivity로 이동
            val intent = Intent(this@BoardDetail, MainActivity::class.java)
            startActivity(intent)
        }

        regUpdate.setOnClickListener {
            val intent = Intent(this@BoardDetail, BoardUpdate::class.java)

            startActivity(intent)
        }

        regDelete.setOnClickListener {
            // 예: 삭제 버튼을 클릭 시 BoardDelete로 이동
            val boardId = intent.getStringExtra("boardId")

            if (boardId != null) {
                val intent = Intent(this@BoardDetail, BoardDelete::class.java)
                intent.putExtra("boardId", boardId)
                startActivity(intent)
            } else {
                Log.e("BoardDetail", "Error: Board ID is null")
            }
        }
        val boardId = intent.getStringExtra("boardId")

        if (boardId != null) {
            getBoardDetail(boardId)
        } else {
            Log.e("BoardDetail", "Error: Board ID is null")
        }
    }
    private fun getBoardDetail(boardId: String) {
        val boardDetailCall: Call<BoardDto> = boardService.getBoardDetail(boardId)

        boardDetailCall.enqueue(object : Callback<BoardDto> {
            override fun onResponse(call: Call<BoardDto>, response: Response<BoardDto>) {
                if (response.isSuccessful) {
                    val boardDetailResponse = response.body()
                    boardDetailResponse?.let {
                        // 상세 정보를 처리하는 로직을 작성하세요.
                        // 예: 받은 데이터를 사용하여 UI에 표시
                        binding.BoardTitle.text = it.title
                        binding.UserNickname.text = it.userNicname
                        binding.boardcontent.text = it.content
                        binding.BarName.text = it.barName
                        binding.UserCount.text = it.memberCount
                        binding.MeetDate.text = it.meetdate.toString()
                        binding.RegDate.text = it.regdate.toString()
                    }
                } else {
                    Log.e("BoardDetail", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BoardDto>, t: Throwable) {
                Log.e("BoardDetail", "Error: ${t.message}")
            }
        })
    }
}