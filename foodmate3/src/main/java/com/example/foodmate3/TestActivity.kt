package com.example.foodmate3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodmate3.databinding.ActivityTestBinding


class TestActivity : BaseActivity() {
    lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initBottomNavigationView()

        // 나머지 코드 작성
        // ...
    }
}