package com.example.chatapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendmessageItemBinding

class SendMessageViewHolder(
    private val binding: SendmessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) {
        itemView.apply {
            binding.sendMessageText.text = message.message
            binding.sendMessageTime.text = message.time
        }
    }
}