package com.example.foodmate2

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmate2.controller.controller.ApiService
import com.example.foodmate2.controller.controller.RetrofitBuilder
import com.example.foodmate2.databinding.ActivityMain3Binding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity3 : AppCompatActivity() {

    private val TAG: String = "MainActivity3"
    private lateinit var binding: ActivityMain3Binding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitBuilder.createApiService()

        binding.btnRegister.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pwRe = binding.editPwRe.text.toString()
            val nickname = binding.editNickname.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if (id.isEmpty() || pw.isEmpty() || nickname.isEmpty()) {
                showDialog("blank")
                return@setOnClickListener
            }

            // 비밀번호가 일치하지 않을 경우
            if (pw != pwRe) {
                showDialog("not same")
                return@setOnClickListener
            }

            // 회원가입 API 호출
            insertMember(id, pw, nickname)

            // 회원가입 버튼 클릭 시 MainActivity3 이동
            val intent = Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)
        }

    }

    private fun insertMember(id: String, pw: String, nickname: String) {
        val call: Call<ResponseBody> = apiService.insertMember("1", id, pw, nickname)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val message = response.code()
                    if (message == 200) {
                        // 회원가입 성공 처리
                        showDialog("성공")
                    } else {
                        // 회원가입 실패 처리
                        showDialog("회원가입 실패")
                    }
                } else {
                    // 서버 오류 등의 상태코드가 반환된 경우
                    showDialog("서버오류로 회원가입 실패")
                }

                Log.d(TAG, "통신 성공 - HTTP 상태 코드: ${response.code()}")
                Log.d(TAG, "통신 성공 - 응답 메시지: ${response.body()?.toString()}")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패 처리
                showDialog("통신 실패")
                Log.e(TAG, "통신 실패: ${t.message}")
            }
        })
    }

    // 회원가입 성공/실패 시 다이얼로그를 띄워주는 메소드
    private fun showDialog(message: String) {
        val dialogBuilder = AlertDialog.Builder(this@MainActivity3)

        dialogBuilder.setTitle("회원가입 결과")
        dialogBuilder.setMessage(message)

        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Log.d(TAG, "확인 버튼 클릭")
                    if (message == "회원가입 성공") {
                        val intent = Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        dialogBuilder.setPositiveButton("확인", dialogListener)
        dialogBuilder.show()
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