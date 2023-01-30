package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.ReceivemessageItemBinding
import java.text.SimpleDateFormat

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message) {
        itemView.apply {
            binding.receiveMessageText.text = message.message
            binding.receiveMessageTime.text = SimpleDateFormat("HH:mm:ss").format(message.time.toLong())
            binding.messageImage.load(message.image)
        }
    }
}