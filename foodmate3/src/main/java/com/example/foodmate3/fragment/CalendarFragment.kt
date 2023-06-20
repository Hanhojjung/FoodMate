package com.example.foodmate3.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmate3.MainActivity
import com.example.foodmate3.R
import com.example.foodmate3.adapter.BoardAdapter
import com.example.foodmate3.adapter.TodoAdapter
import com.example.foodmate3.controller.BoardController
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.controller.TodoController
import com.example.foodmate3.model.BoardDto
import com.example.foodmate3.model.TodoDto
import com.example.foodmate3.network.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class CalendarFragment : Fragment() {
    private val TAG: String = "TodoInsert"

    private lateinit var todoService: TodoController
    private lateinit var memoplus: ImageButton
    private lateinit var titleEditText: EditText
    private lateinit var memoEditText: EditText
    private lateinit var todoAdapter : TodoAdapter
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        memoplus = view.findViewById<ImageButton>(R.id.memoplus)
        todoService = RetrofitBuilder.TodoService()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        recyclerView = view.findViewById(R.id.recyclerView)

        val todoList: MutableList<TodoDto> = mutableListOf()
        todoAdapter = TodoAdapter(requireContext(), todoList)
        recyclerView.adapter = todoAdapter

        getBoardList(todoService)

        memoplus.setOnClickListener {
            showMemoDialog()
        }
    }

    private fun getBoardList(todoService: TodoController) {
        val boardListCall: Call<List<TodoDto>> = todoService.getAllTodo()

        boardListCall.enqueue(object : Callback<List<TodoDto>> {
            override fun onResponse(call: Call<List<TodoDto>>, response: Response<List<TodoDto>>) {
                if (response.isSuccessful) {
                    val boardListResponse = response.body()
                    boardListResponse?.let {
                        todoAdapter.setData(it)
                    }
                } else {
                    Log.e("TodoFragment", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<TodoDto>>, t: Throwable) {
                if (t is IOException) {
                    Log.e("TodoFragment", "Network Error: ${t.message}")
                } else if (t is HttpException) {
                    Log.e("TodoFragment", "HTTP Error: ${t.code()}")
                } else {
                    Log.e("TodoFragment", "Error: ${t.message}")
                }
            }
        })
    }

    private fun showMemoDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_memo)

        titleEditText = dialog.findViewById(R.id.titleEditText)
        memoEditText = dialog.findViewById(R.id.memoEditText)
        val saveButton = dialog.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            dialog.dismiss()
            sendBoardData()
        }

        dialog.show()
    }


    private fun sendBoardData() {
        val userNickname = SharedPreferencesUtil.getSessionNickname(requireContext()) ?: ""
        val title = titleEditText.text.toString()
        val memo = memoEditText.text.toString()
        val todo = TodoDto(
            title,
            "",
            userNickname,
            memo
        )

        val call = todoService.insertTodo(todo)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "응답 코드: ${response.code()}")

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // 전송 실패한 경우의 처리
                    Toast.makeText(requireContext(), "게시글 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "응답 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 통신 실패 처리
                Log.e(TAG, "통신 실패: ${t.message}")
                Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
