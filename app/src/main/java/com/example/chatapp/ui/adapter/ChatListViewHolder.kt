package com.example.chatapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.ChatlistItemBinding

class ChatListViewHolder(
    private val binding: ChatlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {

        val message = chat.messages?.values
            ?.sortedWith(compareBy { it.time })?.last()

        itemView.apply {
//            binding.chatImage.load()
//            binding.chatName.text = chat.name
            binding.chatLastmessage.text = message?.message
        }
    }
}