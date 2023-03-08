package com.example.chatapp.ui.adapter


import android.graphics.Color
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
            if(!chat.message.seen) {
                binding.chatLastmessage.setTextColor(Color.parseColor("#ff0000"))
            }
        }
    }
}