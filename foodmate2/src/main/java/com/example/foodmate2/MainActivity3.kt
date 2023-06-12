package com.example.foodmate2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)


        // 회원가입 버튼 클릭시 MainActivity3 이동

        // 회원가입 버튼 클릭시 MainActivity3 이동
        val joinButton = findViewById<Button>(R.id.btn_register)
        joinButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(intent, 101)
        } // 인텐트에 메뉴액티비티 넣어서 호출

    }

    // 가입 시 토스트
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (data != null) {
                val name = data.getStringExtra("name")
                val toast = Toast.makeText(
                    baseContext,
                    "result code : $resultCode, $name", Toast.LENGTH_LONG
                )
                toast.show()
            }
        }
    }
}