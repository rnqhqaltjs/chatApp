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

        itemView.apply {
            binding.chatImage.load(chat.image)
            binding.chatName.text = chat.name
            binding.chatLastmessage.text = chat.lastmessage
        }
    }
}