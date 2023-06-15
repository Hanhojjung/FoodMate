package com.example.foodmate3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmate3.adapter.BarAdapter
import com.example.foodmate3.controller.BarController
import com.example.foodmate3.databinding.ActivityBarListBinding
import com.example.foodmate3.fragment.CalendarFragment
import com.example.foodmate3.fragment.ChatFragment
import com.example.foodmate3.fragment.HomeFragment
import com.example.foodmate3.fragment.MyFragment
import com.example.foodmate3.model.BarDto
import com.example.foodmate3.network.RetrofitBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class BarListActivity : AppCompatActivity() {
    private val TAG: String = "BarListActivity"
    private var bottomNavigationView: BottomNavigationView? = null

    private lateinit var binding: ActivityBarListBinding
    private lateinit var barService: BarController

    private lateinit var recyclerView: RecyclerView
    private lateinit var barAdapter: BarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 맨 처음 시작할 탭 설정
        binding.bottomNavigationView.selectedItemId = R.id.tab_home

        barService = RetrofitBuilder.BarListService()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@BarListActivity)

        val barList: MutableList<BarDto> = mutableListOf()
        barAdapter = BarAdapter(this@BarListActivity, barList)
        recyclerView.adapter = barAdapter

        getFoodList(barService)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(TabSelectedListener())
    }

    internal inner class TabSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val itemId = menuItem.itemId
            if (itemId == R.id.tab_home) {
                // 홈 프래그먼트를 표시하도록 변경하거나 필요한 대상 프래그먼트로 수정해주세요
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment())
                    .commit()
                return true
            } else if (itemId == R.id.tab_chat) {
                // 채팅 프래그먼트를 표시하도록 변경하거나 필요한 대상 프래그먼트로 수정해주세요
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ChatFragment())
                    .commit()
                return true
            } else if (itemId == R.id.tab_calendar) {
                // 캘린더 프래그먼트를 표시하도록 변경하거나 필요한 대상 프래그먼트로 수정해주세요
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CalendarFragment())
                    .commit()
                return true
            } else if (itemId == R.id.tab_my) {
                // 마이 프래그먼트를 표시하도록 변경하거나 필요한 대상 프래그먼트로 수정해주세요
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, MyFragment())
                    .commit()
                return true
            }
            return false
        }
    }

    private fun getFoodList(barService: BarController) {
        val foodListCall: Call<List<BarDto>> = barService.getAllBars()

        foodListCall.enqueue(object : Callback<List<BarDto>> {
            override fun onResponse(call: Call<List<BarDto>>, response: Response<List<BarDto>>) {
                if (response.isSuccessful) {
                    val barListResponse = response.body()
                    Log.d("lsy","test: " + barListResponse)
                    barListResponse?.let {
                        barAdapter.setData(it)
                    }
                } else {
                    Log.e("BarList", "Error: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<List<BarDto>>, t: Throwable) {
                if (t is IOException) {
                    Log.e("BarList", "Network Error: ${t.message}")
                } else if (t is HttpException) {
                    Log.e("BarList", "HTTP Error: ${t.code()}")
                } else {
                    Log.e("BarList", "Error: ${t.message}")
                }
            }
        })
    }
}