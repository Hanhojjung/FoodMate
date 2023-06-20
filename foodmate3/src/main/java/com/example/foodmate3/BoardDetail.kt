package com.example.foodmate3

import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.foodmate3.Util.MainActivityUtil
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.databinding.ActivityBoardDetailBinding
import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.network.RetrofitBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetail : AppCompatActivity() {

    private val TAG: String = "BoardDetail"
    private lateinit var binding: ActivityBoardDetailBinding
    private lateinit var boardService: BoardController
    private lateinit var context: Context
    private lateinit var menu: Menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //메인 유틸 코드
        MainActivityUtil.initViews(this@BoardDetail)
        val plusButton = findViewById<ImageButton>(R.id.plus)
        plusButton.setOnClickListener {
            MainActivityUtil.showPopupMenu(this, plusButton)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fragmentManager = supportFragmentManager
        val mainLayout = findViewById<View>(R.id.mainLayout)
        MainActivityUtil.setBottomNavigationListener(bottomNavigationView, fragmentManager,mainLayout)

        context = this@BoardDetail

        boardService = RetrofitBuilder.BoardService()

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
        val boardId = intent.getStringExtra("boardId")

        if (boardId != null) {
            getBoardDetail(boardId)
        } else {
            Log.e("BoardDetail", "Error: Board ID is null")
        }
    }

    //메인 유틸 함수 호출
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainActivityUtil.onOptionsItemSelected(this, item)
                || super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        return MainActivityUtil.onCreateOptionsMenu(this@BoardDetail, menu)
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
                        binding.UserNickname.text = it.userNicname
                        binding.boardcontent.text = it.content
                        binding.BarName.text = it.barName
                        binding.UserCount.text = it.memberCount
                        binding.MeetDate.text = it.meetdate.toString()
                        binding.RegDate.text = it.regdate.toString()

                        val urlImg = it.barImg

                        Glide.with(context)
                            .asBitmap()
                            .load(urlImg)
                            .into(object : CustomTarget<Bitmap>(200, 200) {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    binding.BoardImg.setImageBitmap(resource)
                                }
                                override fun onLoadCleared(placeholder: Drawable?) {
                                    // 이미지 로딩이 취소되었을 때의 동작을 정의하려면 여기에 코드를 추가하세요.
                                }
                            })
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