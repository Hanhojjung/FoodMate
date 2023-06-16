package com.example.foodmate3
import androidx.appcompat.widget.Toolbar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.foodmate3.controller.BarController
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ActivityMainBinding
import com.example.foodmate3.model.BarDto
import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.network.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardInsert : AppCompatActivity() {

    val TAG = this.javaClass.simpleName
    private lateinit var toolbar: Toolbar

    private lateinit var txtAppointment: TextView
    private lateinit var btnCalendar: Button

    private lateinit var calendar: Calendar
    private lateinit var dateFormat: DateFormat
    private lateinit var timeFormat: DateFormat

    //식당 리스트
    private lateinit var dropBarList: Spinner
    private lateinit var barService: BarController
    private lateinit var barListResponse: List<BarDto>

    private lateinit var boardService: BoardController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_insert)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val regButton: Button = findViewById(R.id.reg_button)
        val regCancel: Button = findViewById(R.id.reg_cancel)

        //식당 리스트 컨트롤러
        dropBarList = findViewById(R.id.drop_barlist)
        barService = RetrofitBuilder.BarListService()

        //보드 레트로핏 연결
        boardService = RetrofitBuilder.BoardService()

        getRestaurantList(barService)

        regButton.setOnClickListener {
            sendBoardData()
        }

        regCancel.setOnClickListener {
            // 작성 취소 버튼 클릭 시 처리할 로직 작성
            // 예: 메인화면 으로 이동
            val intent = Intent(this@BoardInsert, MainActivity::class.java)
            startActivity(intent)
        }

        txtAppointment = findViewById(R.id.appointment)
        btnCalendar = findViewById(R.id.btn_calendar)

        calendar = Calendar.getInstance()
        dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
        timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        btnCalendar.setOnClickListener {
            showDatePicker()

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

    //식당이름 리스트 불러오기
    private fun getRestaurantList(barService: BarController) {
        val restaurantListCall: Call<List<BarDto>> = barService.getAllBars()

        restaurantListCall.enqueue(object : Callback<List<BarDto>> {
            override fun onResponse(call: Call<List<BarDto>>, response: Response<List<BarDto>>) {
                if (response.isSuccessful) {
                    barListResponse = response.body() ?: emptyList() // 식당 목록을 barListResponse에 할당
                    // 식당 이름만 추출하여 어댑터에 설정
                    val restaurantNames = barListResponse.map { bar -> bar.main_TITLE }
                    val adapter = ArrayAdapter(
                        this@BoardInsert,
                        android.R.layout.simple_spinner_item,
                        restaurantNames
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dropBarList.adapter = adapter
                } else {
                    Log.e("RestaurantList", "Error: ${response.code()}")
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
    private fun getSelectedBarImageUrl(barName: String): String {
        val selectedBar = barListResponse.firstOrNull { it.main_TITLE == barName }
        return selectedBar?.main_IMG_NORMAL ?: ""
    }

    // 날짜 선택
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    // 시간 선택
    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // 선택한 날짜와 시간을 @+id/appointment 텍스트뷰에 표시
                val appointmentDateTime = dateFormat.format(calendar.time)
                    .toString() + " " + timeFormat.format(calendar.time).toString()
                txtAppointment.text = appointmentDateTime

                // 선택한 날짜와 시간을 다른 방식으로 활용하려면 이곳에 원하는 동작을 추가
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            android.text.format.DateFormat.is24HourFormat(applicationContext)
        )

        timePickerDialog.show()
    }

    private fun sendBoardData() {
        val intent = Intent(this@BoardInsert, BoardDetail::class.java)
        startActivity(intent)
        val userNicname = SharedPreferencesUtil.getSessionNickname(this@BoardInsert) // 작성자 정보
        val title = findViewById<EditText>(R.id.boardtitle).text.toString()
        val content = findViewById<EditText>(R.id.boardcontent).text.toString()
        val barName = dropBarList.selectedItem.toString() // 선택된 식당 이름
        val barImg = getSelectedBarImageUrl(barName) // 해당 식당 이미지 URL 가져오기
        val memberCount = findViewById<EditText>(R.id.partyone).text.toString()
        val meetdate = dateFormat.format(calendar.time).toString() // 만남 날짜
        val regdate =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()) // 등록 날짜

        boardService = RetrofitBuilder.BoardService()
        val call = userNicname?.let {
            boardService.insertBoard(
                it,
                title,
                content,
                barName,
                barImg,
                memberCount,
                meetdate,
                regdate
            )
        }

        if (call != null) {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "응답 코드: ${response.code()}")
                    } else {
                        // 전송 실패한 경우의 처리
                        Toast.makeText(applicationContext, "게시글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "응답 코드: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 통신 실패 처리
                    Log.e(TAG, "통신 실패: ${t.message}")
                    Toast.makeText(applicationContext, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}