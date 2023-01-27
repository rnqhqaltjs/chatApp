package com.example.chatapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.ReceivemessageItemBinding

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) {
        itemView.apply {
            binding.receiveMessageText.text = message.message
            binding.receiveMessageTime.text = message.time
        }
    }
}