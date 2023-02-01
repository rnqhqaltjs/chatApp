package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendmessageItemBinding
import java.text.SimpleDateFormat

class SendMessageViewHolder(
    private val binding: SendmessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message) {
        itemView.apply {
            binding.sendMessageText.text = message.message
            binding.sendMessageTime.text = SimpleDateFormat("HH:mm:ss").format(message.time?.toLong())
        }
    }
}