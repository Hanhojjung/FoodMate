package com.example.foodmate3.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.ItemListBinding
import com.example.foodmate3.model.TodoDto


class TodoViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

class TodoAdapter(private val context: Context, private val todoList: MutableList<TodoDto>) :
    RecyclerView.Adapter<TodoViewHolder>() {
    private val sessionNickname = SharedPreferencesUtil.getSessionNickname(context)

    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):TodoViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val binding = holder.binding
        val todo = todoList?.get(position)

        binding.title.text = todo?.title
        binding.contents.text = todo?.memo


    }

    fun setData(data: List<TodoDto>?) {
        todoList.clear()
        if (data != null) {
            todoList.addAll(data)
        }
        notifyDataSetChanged()
    }
}