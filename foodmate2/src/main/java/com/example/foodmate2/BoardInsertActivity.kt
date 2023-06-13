package com.example.foodmate2


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.DateFormat
import java.util.Calendar

class BoardInsertActivity : AppCompatActivity() {

    private lateinit var txtAppointment: TextView
    private lateinit var btnCalendar: Button

    private lateinit var calendar: Calendar
    private lateinit var dateFormat: DateFormat
    private lateinit var timeFormat: DateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_insert)
        val regButton: Button = findViewById(R.id.reg_button)
        val regCancel: Button = findViewById(R.id.reg_cancel)

        regButton.setOnClickListener {
            // 등록하기 버튼 클릭 시 처리할 로직 작성
            // 예: BoardList로 이동
            val intent = Intent(this@BoardInsertActivity, BoardDetail::class.java)
            startActivity(intent)
        }

        regCancel.setOnClickListener {
            // 작성 취소 버튼 클릭 시 처리할 로직 작성
            // 예: 메인화면 으로 이동
            val intent = Intent(this@BoardInsertActivity, MainActivity::class.java)
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



    // 날짜 선택
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

    // 시간 선택
    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(this,
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
            android.text.format.DateFormat.is24HourFormat(applicationContext))

        timePickerDialog.show()
    }
}
