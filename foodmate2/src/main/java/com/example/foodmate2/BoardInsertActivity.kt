package com.example.foodmate2


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.foodmate2.controller.controller.ApiService
import com.example.foodmate2.controller.controller.Board
import com.example.foodmate2.controller.controller.RetrofitBuilder
import com.example.foodmate2.databinding.ActivityBoardInsertBinding
import com.example.foodmate2.databinding.ActivityMain2Binding
import com.example.foodmate2.databinding.ActivityMain3Binding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.Calendar

class BoardInsertActivity : AppCompatActivity() {

    private lateinit var board: Board
    private val TAG: String = "BoardInsertActivity"
    private lateinit var binding: ActivityBoardInsertBinding
    private lateinit var apiService: ApiService


    private lateinit var txtAppointment: TextView
    private lateinit var btnCalendar: Button

    private lateinit var calendar: Calendar
    private lateinit var dateFormat: DateFormat
    private lateinit var timeFormat: DateFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_board_insert)
        binding = ActivityBoardInsertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitBuilder.createApiService()

        // 수정된 부분: findViewById를 제거하고 binding 객체에서 직접 접근
        binding.regButton.setOnClickListener {
            val title = binding.titleTv.text.toString()
            val user_count = binding.partyone.text.toString()
            val bar_name = binding.restaurant.text.toString()
            val bar_appoint = binding.appointment.text.toString()
            val content = binding.contentTv.text.toString()

            insertBoard(title, user_count, bar_name, bar_appoint, content)

            val intent = Intent(this@BoardInsertActivity, BoardDetail::class.java)
            intent.putExtra("title", title)
            intent.putExtra("user_count", user_count)
            intent.putExtra("bar_name", bar_name)
            intent.putExtra("bar_appoint", bar_appoint)
            intent.putExtra("content", content)
            startActivity(intent)
        }

        // 수정된 부분: findViewById를 제거하고 binding 객체에서 직접 접근
        binding.regCancel.setOnClickListener {
            val intent = Intent(this@BoardInsertActivity, MainActivity::class.java)
            startActivity(intent)
        }


        txtAppointment = binding.appointment
        btnCalendar = binding.btnCalendar


        calendar = Calendar.getInstance()
        dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
        timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        btnCalendar.setOnClickListener {
            showDatePicker()

        }
    }
    private fun insertBoard(title: String, user_count: String, bar_name: String, bar_appoint : String, content : String) {
        val call: Call<ResponseBody> = apiService.insertBoard("1", title, user_count, bar_name,bar_appoint ,content)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // 서버 요청 성공 처리
                Log.d(TAG, "게시판 삽입 성공")

                // BoardDetail로 이동하는 로직 추가
                val intent = Intent(this@BoardInsertActivity, BoardDetail::class.java)
                intent.putExtra("title", title)
                intent.putExtra("user_count", user_count)
                intent.putExtra("bar_name", bar_name)
                intent.putExtra("bar_appoint", bar_appoint)
                intent.putExtra("content", content)
                startActivity(intent)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패 처리
                Log.e(TAG, "게시판 삽입 실패: ${t.message}")

            }
        })
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

