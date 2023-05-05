package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.ReceivemessageItemBinding
import java.text.SimpleDateFormat

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message, isDateHeaderNeeded: Boolean) {
        itemView.apply {
            binding.receiveMessageText.text = message.message
            binding.receiveMessageTime.text = SimpleDateFormat("hh:mm a").format(message.time.toLong())
            binding.messageImage.load(message.image)

            if (isDateHeaderNeeded) {
                binding.receiveMessageDate.visibility = View.VISIBLE
                binding.receiveMessageDate.text = SimpleDateFormat("yyyy년 MM월 dd일").format(message.time.toLong())
            } else {
                binding.receiveMessageDate.visibility = View.GONE
            }
        }
    }
}