package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendmessageItemBinding
import java.text.SimpleDateFormat

class SendMessageViewHolder(
    private val binding: SendmessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message, isFirstDate: Boolean, isFirstTime: Boolean) {
        itemView.apply {
            binding.sendMessageText.text = message.message

            if(message.seen){
                binding.sendMessageDate.visibility = View.GONE
            } else {
                binding.sendMessageTime.visibility = View.VISIBLE
            }

            if(isFirstTime) {
                binding.sendMessageTime.visibility = View.VISIBLE
                binding.sendMessageTime.text = SimpleDateFormat("hh:mm a").format(message.time.toLong())
            } else {
                binding.sendMessageDate.visibility = View.GONE
            }

            if (isFirstDate) {
                binding.sendMessageDate.visibility = View.VISIBLE
                binding.leftLine.visibility = View.VISIBLE
                binding.rightLine.visibility = View.VISIBLE
                binding.sendMessageDate.text = SimpleDateFormat("yyyy년 MM월 dd일").format(message.time.toLong())
            } else {
                binding.sendMessageDate.visibility = View.GONE
                binding.leftLine.visibility = View.GONE
                binding.rightLine.visibility = View.GONE
            }
        }
    }
}