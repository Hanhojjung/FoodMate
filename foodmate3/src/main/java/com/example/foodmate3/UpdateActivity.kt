package com.example.foodmate3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.foodmate3.controller.MemberController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ActivityUpdateBinding
import com.example.foodmate3.fragment.MyFragment
import com.example.foodmate3.model.MemberDto
import com.example.foodmate3.model.UpdateResponse
import com.example.foodmate3.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {

    private val TAG: String = "UpdateActivity"
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var apiService: MemberController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionId = SharedPreferencesUtil.getSessionId(this)
        val sessionPw = SharedPreferencesUtil.getSessionPw(this)
        val sessionNickname = SharedPreferencesUtil.getSessionNickname(this)

        binding.editId.setText(sessionId)
        binding.editPw.setText(sessionPw)
        binding.editNickname.setText(sessionNickname)

        apiService = RetrofitBuilder.MemberService()

        // 회원 정보 수정 버튼
        binding.btnUpdate.setOnClickListener {
            val id = binding.editId.text.toString()
            val password = binding.editPw.text.toString()
            val nickname = binding.editNickname.text.toString()

            SharedPreferencesUtil.updateSession(this, id, password, nickname)
            Log.d(TAG, "저장된 세션값: $id, $password, $nickname")

            SharedPreferencesUtil.reloadSession(this, id, password, nickname)
            Log.d(TAG, "리로드된  세션값: $id, $password, $nickname")

            val member = MemberDto(id, password, nickname)
            updateMemberInfo(member)

            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 회원 탈퇴 버튼
        binding.btnDelete.setOnClickListener {
            val id = binding.editId.text.toString()
            val password = binding.editPw.text.toString()
            val nickname = binding.editNickname.text.toString()

            SharedPreferencesUtil.clearSession(this)
            Log.d(TAG, "삭제된 세션값: $id, $password, $nickname")

            SharedPreferencesUtil.reloadSession(this,id,password,nickname)
            Log.d(TAG, "리로드된 세션값: $id, $password, $nickname")

            deleteMember(id, password, nickname)

            val intent = Intent(this@UpdateActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateMemberInfo(member: MemberDto) {
        apiService.updateMember(member).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val updateResponse = response.body()
                    if (updateResponse != null && updateResponse == "success") {
                        Log.d(TAG, "회원 정보 수정 완료")
                        // Handle success
                    } else {
                        val message = updateResponse ?: "회원 정보 수정 실패"
                        Log.d(TAG, message)
                        // Handle failure
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = "회원 정보 수정 실패 - ${response.code()}: $errorBody"
                    Log.d(TAG, message)
                    // Handle failure
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "통신 실패: ${t.message}")
                // Handle failure
            }
        })
    }

    private fun deleteMember(id: String, password: String, nickname: String) {
        val deleteMemberController = DeleteMemberController()

        deleteMemberController.deleteMember(this, id, password, nickname) { isSuccess ->
            if (isSuccess) {
                runOnUiThread {
                    Log.d(TAG, "회원 탈퇴 성공")
                    val intent = Intent(this@UpdateActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                runOnUiThread {
                    Log.d(TAG, "회원 탈퇴 실패")
                }
            }
        }
    }

}
