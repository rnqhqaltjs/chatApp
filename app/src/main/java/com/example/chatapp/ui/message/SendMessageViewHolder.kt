package com.example.chatapp.ui.message

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.SendmessageItemBinding
import java.text.SimpleDateFormat

class SendMessageViewHolder(
    private val binding: SendmessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(viewModel: MessageViewModel, message: Message, isFirstDate: Boolean, isFirstTime: Boolean) {
        binding.viewmodel = viewModel
        binding.message = message
        binding.isFirstDate = isFirstDate
        binding.isFirstTime = isFirstTime
        binding.executePendingBindings()
    }
}