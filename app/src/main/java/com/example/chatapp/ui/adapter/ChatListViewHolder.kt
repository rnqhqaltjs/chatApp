package com.example.chatapp.ui.adapter


import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Chat
import com.example.chatapp.databinding.ChatlistItemBinding
import com.example.chatapp.util.getLastMessageTimeString

class ChatListViewHolder(
    private val binding: ChatlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {

        itemView.apply {
            binding.chatImage.load(chat.user.image)
            binding.chatName.text = chat.user.name
            binding.chatLastmessage.text = chat.message.message
            binding.chatTime.text = getLastMessageTimeString(chat.message.time.toLong())
        }
    }
}