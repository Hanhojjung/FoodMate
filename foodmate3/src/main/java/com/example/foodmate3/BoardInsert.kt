package com.example.foodmate3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.example.foodmate3.controller.SharedPreferencesUtil
import java.text.DateFormat
import java.util.Calendar

class BoardInsert : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
//    private lateinit var titleEditText: EditText
//    private lateinit var dateEditText: EditText
//    private lateinit var partyoneEditText: EditText
//    private lateinit var appointmentEditText: EditText
//    private lateinit var restaurantEditText: EditText
//    private lateinit var contentEditText: EditText
//    private lateinit var saveButton: Button
//
    private lateinit var txtAppointment: TextView
    private lateinit var btnCalendar: Button
//
    private lateinit var calendar: Calendar
    private lateinit var dateFormat: DateFormat
    private lateinit var timeFormat: DateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_insert)

//        titleEditText = findViewById(R.id.title_et)
//        dateEditText = findViewById(R.id.date_et)
//        partyoneEditText = findViewById(R.id.partyone_et)
//        appointmentEditText = findViewById(R.id.appointment_et)
//        restaurantEditText = findViewById(R.id.restaurant_et)
//        contentEditText = findViewById(R.id.content_et)
//        saveButton = findViewById(R.id.reg_button)


        val regButton:Button=findViewById(R.id.reg_button)
        val regCancel: Button = findViewById(R.id.reg_cancel)


        regButton.setOnClickListener {
            saveBoardData()
        }

        regCancel.setOnClickListener {
            val intent = Intent(this@BoardInsert, MainActivity::class.java)
            startActivity(intent)
        }

        txtAppointment = findViewById(R.id.appointment_et)
        btnCalendar = findViewById(R.id.btn_calendar)

        calendar = Calendar.getInstance()
        dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
        timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        btnCalendar.setOnClickListener {
            showDatePicker()
        }

        val isLoggedIn = SharedPreferencesUtil.checkLoggedIn(this)
        Log.d(TAG, "세션 유지 상태: $isLoggedIn")

        if (!isLoggedIn) {
            // 사용자가 로그인되어 있지 않으면 MainActivity2(로그인 화면)로 이동합니다.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val appointmentDateTime = dateFormat.format(calendar.time).toString() + " " + timeFormat.format(calendar.time).toString()
                txtAppointment.text = appointmentDateTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            android.text.format.DateFormat.is24HourFormat(applicationContext))

        timePickerDialog.show()
    }

    private fun saveBoardData() {
//        val title = titleEditText.text.toString()
//        val date = dateEditText.text.toString()
//        val partyone = partyoneEditText.text.toString()
//        val appointment = appointmentEditText.text.toString()
//        val restaurant = restaurantEditText.text.toString()
//        val content = contentEditText.text.toString()
//
//        val intent = Intent(this@BoardInsert, BoardDetail::class.java)
//        intent.putExtra("title", title)
//        intent.putExtra("date", date)
//        intent.putExtra("partyone", partyone)
//        intent.putExtra("appointment", appointment)
//        intent.putExtra("restaurant", restaurant)
//        intent.putExtra("content", content)
//        startActivity(intent)
    }
}
