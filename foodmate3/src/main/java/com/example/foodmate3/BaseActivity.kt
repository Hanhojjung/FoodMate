package com.example.foodmate3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun initToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    protected fun initBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.setOnNavigationItemSelectedListener(TabSelectedListener())
    }

    internal inner class TabSelectedListener :
        BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
            val itemId = menuItem.itemId
            if (itemId == R.id.tab_home) {
                // 홈 탭 선택 시 처리
                return true
            } else if (itemId == R.id.tab_chat) {
                // 채팅 탭 선택 시 처리
                return true
            } else if (itemId == R.id.tab_calendar) {
                // 캘린더 탭 선택 시 처리
                return true
            } else if (itemId == R.id.tab_my) {
                // 마이 탭 선택 시 처리
                return true
            }
            return false
        }
    }

    protected fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.board_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == R.id.writing) {
                // 모집글 작성 메뉴 클릭 시 처리할 로직 작성
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.mywriteview) {
                // 내가 작성한 글 보기 메뉴 클릭 시 처리할 로직 작성
                return@setOnMenuItemClickListener true
            } else if (itemId == R.id.barlist) {
                // 바 리스트 보기 메뉴 클릭 시 처리할 로직 작성
                return@setOnMenuItemClickListener true
            }
            false
        }
        popupMenu.show()
    }

    // 툴바 관련 메소드

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.login) {
            // 로그인 메뉴 클릭 시 처리
            return true
        } else if (itemId == R.id.logout) {
            // 로그아웃 메뉴 클릭 시 처리
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
