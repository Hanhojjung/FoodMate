package com.example.foodmate2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    final String TAG = this.getClass().getSimpleName();

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "개발새발(https://jeong9216.tistory.com/) 블로그 좋아요!");


        init(); // 객체 정의
        SettingListener(); // 리스너 등록

        // 맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.tab_home);

        Toolbar toolbar = findViewById(R.id.toolbar); // 툴바 정의
        setSupportActionBar(toolbar);

        // 플러스 버튼 클릭시 팝업 메뉴
        ImageButton plusButton = findViewById(R.id.plus);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(plusButton);
            }
        });

    }


    // 메뉴 리소스 XML의 내용을 앱바(App Bar)에 반영
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.login) {
            startActivity(new Intent(this, MainActivity2.class));
            return true;
        } else if (itemId == R.id.logout) {
            // 로그아웃 동작 처리
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init() {
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
        // 선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.tab_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new HomeFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.tab_chat) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new ChatFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.tab_calendar) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new CalendarFragment())
                        .commit();
                return true;
            } else if (itemId == R.id.tab_my) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_ly, new MyFragment())
                        .commit();
                return true;
            }

            return false;
        }
    }


    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.board_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.writing) {
                    // 모집글 작성 메뉴 클릭 시 처리할 로직 작성
                    // 예: BoardInsertActivity로 이동
                    Intent intent = new Intent(MainActivity.this, BoardInsertActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.mywriteview) {
                    // 내가 작성한 글 보기 메뉴 클릭 시 처리할 로직 작성
                    // 예: MyWritePage로 이동
                    Intent intent = new Intent(MainActivity.this, MyWritePage.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}