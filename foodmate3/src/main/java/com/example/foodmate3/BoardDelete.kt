package com.example.foodmate3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmate3.Util.MainActivityUtil
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.databinding.ActivityBoardDeleteBinding
import com.example.foodmate3.network.RetrofitBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDelete : AppCompatActivity() {

    private lateinit var binding: ActivityBoardDeleteBinding
    private lateinit var boardService: BoardController
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardService = RetrofitBuilder.BoardService()

        val boardId = intent.getStringExtra("boardId")

        if (boardId != null) {
            deleteBoard(boardId)
        } else {
            Log.e("BoardDelete", "Error: Board ID is null")
        }
        //메인 유틸 코드
        MainActivityUtil.initViews(this@BoardDelete)
        val plusButton = findViewById<ImageButton>(R.id.plus)
        plusButton.setOnClickListener {
            MainActivityUtil.showPopupMenu(this, plusButton)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fragmentManager = supportFragmentManager
        val mainLayout = findViewById<View>(R.id.mainLayout)
        MainActivityUtil.setBottomNavigationListener(bottomNavigationView, fragmentManager,mainLayout)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MainActivityUtil.onOptionsItemSelected(this, item)
                || super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        return MainActivityUtil.onCreateOptionsMenu(this@BoardDelete, menu)
    }

    private fun deleteBoard(boardId: String) {
        val deleteBoardCall: Call<ResponseBody> = boardService.deleteBoard(boardId)

        deleteBoardCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Board deleted successfully
                    // Handle the response, e.g., display a success message
                    Log.d("BoardDelete", "Board deleted successfully")

                    val intent = Intent(this@BoardDelete, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("BoardDelete", "Error: ${response.code()}")
                    // Handle the error response, e.g., display an error message
                }
                finish() // Finish the BoardDelete activity after deletion
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("BoardDelete", "Error: ${t.message}")
                // Handle the failure, e.g., display an error message
                finish() // Finish the BoardDelete activity after failure
            }
        })
    }
}
