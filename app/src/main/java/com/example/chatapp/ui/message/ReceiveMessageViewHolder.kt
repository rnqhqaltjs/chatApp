package com.example.chatapp.ui.message

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.ReceivemessageItemBinding

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(viewModel: MessageViewModel, message: Message, isFirstDate: Boolean, isFirstTime: Boolean, user: User) {
        binding.viewmodel = viewModel
        binding.message = message
        binding.user = user
        binding.isFirstDate = isFirstDate
        binding.isFirstTime = isFirstTime
        binding.executePendingBindings()
    }
}