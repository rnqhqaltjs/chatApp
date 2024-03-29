package com.example.chatapp.ui.message

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendmessageItemBinding

class SendMessageViewHolder(
    private val binding: SendmessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: MessageViewModel, message: Message, isFirstDate: Boolean, isFirstTime: Boolean) {
        binding.viewmodel = viewModel
        binding.message = message
        binding.isFirstDate = isFirstDate
        binding.isFirstTime = isFirstTime
        binding.executePendingBindings()
    }
}