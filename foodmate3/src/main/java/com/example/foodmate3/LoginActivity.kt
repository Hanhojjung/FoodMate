package com.example.foodmate3

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.example.foodmate3.controller.MemberController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ActivityLoginBinding
import com.example.foodmate3.model.LoginResponse
import com.example.foodmate3.model.MemberDto
import com.example.foodmate3.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val TAG: String = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiService: MemberController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitBuilder.MemberService()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val loginButton = binding.btnLogin
        loginButton.setOnClickListener {
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()


            if (id.isEmpty() || pw.isEmpty()) {
                showDialog("blank")
                return@setOnClickListener
            }

            val member = MemberDto(id, pw, nickname = "")
            val nickname = member.nickname
            login(member)
        }

        val joinButton = binding.btnRegister
        joinButton.setOnClickListener {
            val intent = Intent(applicationContext, JoinActivity::class.java)
            startActivityForResult(intent, 101)
        }
    }

    private fun login(member: MemberDto) {
        val call = apiService.login(member)
        Log.d(TAG, "로그인 요청 - ID: ${member.id}, PW: ${member.pw}")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.message == "Login successful") {
                        // 로그인 성공 처리
                        Log.d(TAG, "로그인 성공")
                        showDialog("success")

                        val sessionId = loginResponse?.sessionId ?: ""
                        val sessionPw = loginResponse?.sessionPw ?: ""
                        val nickname = loginResponse?.nickname ?: ""

                        // 세션 ID 저장
                        SharedPreferencesUtil.saveSessionId(this@LoginActivity, sessionId, sessionPw, nickname)
                        Log.d(TAG, "Saving session ID: $sessionId, session PW: $sessionPw, nickname: $nickname")

                        // 로그인 상태를 true로 설정합니다.
                        SharedPreferencesUtil.setLoggedIn(this@LoginActivity, true)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 로그인 실패 처리
                        Log.d(TAG, "로그인 실패")
                        showDialog("fail")
                    }
                } else {
                    // API 요청 실패 처리
                    Log.d(TAG, "API 요청 실패")
                    showDialog("fail")
                }
                Log.d(TAG, "통신 성공 - HTTP 상태 코드: ${response.code()}")
                Log.d(TAG, "통신 성공 - 응답 메시지: ${response.body()?.toString()}")
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // 통신 실패 처리
                showDialog("fail")
                Log.e(TAG, "통신 실패: ${t.message}")
            }
        })
    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    private fun showDialog(type: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        if (type == "success") {
            dialogBuilder.setTitle("로그인 성공")
            dialogBuilder.setMessage("로그인 성공!")
        } else if (type == "fail") {
            dialogBuilder.setTitle("로그인 실패")
            dialogBuilder.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Log.d(TAG, "확인 버튼 클릭")
                    if (type == "success") {
                        // 로그인 성공 시 처리할 작업 수행
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        dialogBuilder.setPositiveButton("확인", dialogListener)
        val dialog = dialogBuilder.create() // 다이얼로그 객체 생성
        dialog.show() // 다이얼로그 표시
    }

}