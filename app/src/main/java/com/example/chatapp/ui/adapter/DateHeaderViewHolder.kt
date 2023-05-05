package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.DateheaderItemBinding
import java.text.SimpleDateFormat

class DateHeaderViewHolder(
    private val binding: DateheaderItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message) {
        itemView.apply {

            binding.textView.text = SimpleDateFormat("yyyy년 MM월 dd일").format(message.time.toLong())
        }
    }
}