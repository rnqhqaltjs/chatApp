package com.example.chatapp.ui.message

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendimageItemBinding

class SendImageViewHolder(
    private val binding: SendimageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: MessageViewModel, message: Message, isFirstDate: Boolean, isFirstTime: Boolean) {
        binding.viewmodel = viewModel
        binding.message = message
        binding.isFirstDate = isFirstDate
        binding.isFirstTime = isFirstTime
        binding.executePendingBindings()
    }
}