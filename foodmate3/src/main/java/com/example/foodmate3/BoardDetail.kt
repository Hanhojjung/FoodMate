package com.example.foodmate3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodmate3.controller.SharedPreferencesUtil


class BoardDetail : AppCompatActivity() {
    val TAG = this.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_board_detail)

        val isLoggedIn = SharedPreferencesUtil.checkLoggedIn(this)
        Log.d(TAG, "세션 유지 상태: $isLoggedIn")
        if (!isLoggedIn) {
            // 사용자가 로그인되어 있지 않으면 MainActivity2(로그인 화면)로 이동합니다.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()


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
        }

    }
    // 메뉴 리소스 XML의 내용을 앱바(App Bar)에 반영
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)

        // MainActivity의 onCreateOptionsMenu 함수 내에서 호출하여 세션 유지 상태를 확인하는 예시입니다.
        val isLoggedIn = SharedPreferencesUtil.checkLoggedIn(this)
        val loginMenuItem = menu.findItem(R.id.login)
        val logoutMenuItem = menu.findItem(R.id.logout)

        if (isLoggedIn) {
            // 로그인 상태인 경우
            loginMenuItem.isVisible = false // 로그인 메뉴 숨기기
            logoutMenuItem.isVisible = true // 로그아웃 메뉴 보이기
        } else {
            // 로그아웃 상태인 경우
            loginMenuItem.isVisible = true // 로그인 메뉴 보이기
            logoutMenuItem.isVisible = false // 로그아웃 메뉴 숨기기
        }
        return true
    }


    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.login) {
            // 사용자가 이미 로그인 화면에 있으므로 다시 이동할 필요가 없습니다.
            return true
        } else if (itemId == R.id.logout) {
            // 로그아웃 처리
            SharedPreferencesUtil.setLoggedIn(this, false) // 로그인 상태를 false로 설정합니다.
            invalidateOptionsMenu() // 옵션 메뉴를 다시 그리도록 호출합니다.

            // MainActivity2(로그인 화면)로 이동합니다.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}