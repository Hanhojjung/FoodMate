package com.example.foodmate3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.foodmate3.controller.BarController
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ActivityBoardUpdateBinding
import com.example.foodmate3.model.BarDto
import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.network.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardUpdate : AppCompatActivity() {

    private val TAG: String = "BoardUpdate"
    private lateinit var boardDto: BoardDto
    private lateinit var binding: ActivityBoardUpdateBinding
    private lateinit var calendar: Calendar
    private lateinit var dateFormat: java.text.DateFormat
    private lateinit var timeFormat: java.text.DateFormat
    private lateinit var txtAppointment: TextView
    private lateinit var btnCalendar: Button

    // 식당 리스트
    private lateinit var dropBarList: Spinner
    private lateinit var barService: BarController
    private lateinit var barListResponse: List<BarDto>

    private lateinit var boardService: BoardController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val regUpdate: Button = findViewById(R.id.reg_update)

        // Intent로부터 BoardDto 객체를 가져옴
        boardDto = intent.getSerializableExtra("boardDto") as BoardDto

        // 가져온 정보를 UI에 표시
        binding.boardtitle.setText(boardDto.title)
        binding.boardcontent.setText(boardDto.content)
        binding.partyone.setText(boardDto.memberCount)
        binding.appointment.setText(boardDto.meetdate)
        // 필요한 정보들을 각각의 UI 요소에 맞게 표시

        // 업데이트 버튼 클릭 이벤트 처리
        binding.regUpdate.setOnClickListener {
            // 수정된 정보를 가져옴
            val updatedTitle = binding.boardtitle.text.toString()
            val updatedContent = binding.boardcontent.text.toString()
            val updatedPartyOne = binding.partyone.text.toString()

            // 필요한 정보들을 각각의 UI 요소에서 가져옴

            // 수정된 정보를 서버에 업데이트하는 로직을 구현
            updateBoard(boardDto.boardid, updatedTitle, updatedContent, updatedPartyOne)
        }

        txtAppointment = findViewById(R.id.appointment)
        btnCalendar = findViewById(R.id.btn_calendar)

        calendar = Calendar.getInstance()
        dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext)
        timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        btnCalendar.setOnClickListener {
            showDatePicker()
        }

        // 식당 리스트 컨트롤러
        dropBarList = findViewById(R.id.drop_barlist)
        barService = RetrofitBuilder.BarListService()

        // 보드 레트로핏 연결
        boardService = RetrofitBuilder.BoardService()

        getRestaurantList(barService)
    }

    // 식당 이름 리스트 불러오기
    private fun getRestaurantList(barService: BarController) {
        val restaurantListCall: Call<List<BarDto>> = barService.getAllBars()

        restaurantListCall.enqueue(object : Callback<List<BarDto>> {
            override fun onResponse(call: Call<List<BarDto>>, response: Response<List<BarDto>>) {
                if (response.isSuccessful) {
                    barListResponse = response.body() ?: emptyList() // 식당 목록을 barListResponse에 할당
                    // 식당 이름만 추출하여 어댑터에 설정
                    val restaurantNames = barListResponse.map { bar -> bar.main_TITLE }
                    val adapter = ArrayAdapter(
                        this@BoardUpdate,
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
            DateFormat.is24HourFormat(applicationContext)
        )

        timePickerDialog.show()
    }

    private fun updateBoard(
        boardId: String,
        updatedTitle: String,
        updatedContent: String,
        updatedPartyOne: String
    ) {
        val userNickname = SharedPreferencesUtil.getSessionNickname(this@BoardUpdate) ?: "" // 작성자 정보
        val barName = dropBarList.selectedItem.toString() // 선택된 식당 이름
        val barImg = getSelectedBarImageUrl(barName) // 선택된 식당의 이미지 URL
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date()) // 현재 날짜와 시간

        val updatedBoardDto = BoardDto(
            boardId,
            userNickname,
            updatedTitle,
            updatedContent,
            barName,
            barImg,
            updatedPartyOne,
            txtAppointment.text.toString(),
            currentDateAndTime
        )

        val updateBoardCall: Call<ResponseBody> = boardService.updateBoard(boardId, updatedBoardDto)


        updateBoardCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@BoardUpdate,
                        "게시글이 업데이트되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@BoardUpdate, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t is IOException) {
                    Log.e(TAG, "Network Error: ${t.message}")
                } else if (t is HttpException) {
                    Log.e(TAG, "HTTP Error: ${t.code()}")
                } else {
                    Log.e(TAG, "Error: ${t.message}")
                }
            }
        })
    }

}
