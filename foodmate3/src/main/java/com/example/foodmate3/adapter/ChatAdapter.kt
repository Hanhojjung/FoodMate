package com.example.foodmate3.adapter

import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodmate3.R
import com.example.foodmate3.databinding.ItemMessageBinding
import com.example.foodmate3.model.MessageDto

class ChatViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

class ChatAdapter(private val context: Context, private val messages: MutableList<MessageDto>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun submitList(newList: List<Message>) {
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ChatViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageDto) {
            binding.chatMsgLayoutBg.findViewById<TextView>(R.id.chatMsg).text = message.content
        }
    }
}

private fun <E> MutableList<E>.addAll(elements: List<Message>) {

}

