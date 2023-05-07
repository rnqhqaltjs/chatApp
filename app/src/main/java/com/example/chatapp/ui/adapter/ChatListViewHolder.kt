package com.example.chatapp.ui.adapter


import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Chat
import com.example.chatapp.databinding.ChatlistItemBinding
import com.example.chatapp.util.getLastMessageTimeString
import com.google.firebase.auth.FirebaseAuth

class ChatListViewHolder(
    private val binding: ChatlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {

        itemView.apply {
            binding.chatImage.load(chat.user.image)
            binding.chatImage.clipToOutline = true
            binding.chatName.text = chat.user.name
            binding.chatLastmessage.text = chat.message.message
            binding.chatTime.text = getLastMessageTimeString(chat.message.time.toLong())

            if(!chat.message.seen && chat.message.sendId != FirebaseAuth.getInstance().currentUser?.uid) {
                binding.chatLastmessage.setTextColor(Color.parseColor("#99CCFF"))
                binding.chatLastmessage.setTypeface(null, Typeface.BOLD)
                binding.newmessagenotify.isVisible = true
            }

        }
    }
}