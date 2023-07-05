package com.example.chatapp.ui.chat


import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Chat
import com.example.chatapp.databinding.ChatlistItemBinding

class ChatListViewHolder(
    private val binding: ChatlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ChatViewModel, chat: Chat) {
        binding.viewmodel = viewModel
        binding.chat = chat
        binding.executePendingBindings()

    }
}