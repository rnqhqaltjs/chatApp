package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import androidx.core.view.isVisible
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
            binding.sendMessageTime.text = SimpleDateFormat("hh:mm a").format(message.time.toLong())
            if(message.seen) {
                binding.sendMessageSeen.isVisible = true
            }
        }
    }
}