package com.example.chatapp.ui.message

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.ReceivemessageItemBinding
import java.text.SimpleDateFormat

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(viewModel: MessageViewModel, message: Message, isDateHeaderNeeded: Boolean, isFirstTime: Boolean, user: User) {
        binding.viewmodel = viewModel
        binding.message = message
        binding.executePendingBindings()

        if(isFirstTime) {
            binding.receiveMessageTime.visibility = View.VISIBLE
            binding.receiveMessageTime.text = SimpleDateFormat("hh:mm a").format(message.time.toLong())
        } else {
            binding.receiveMessageTime.visibility = View.GONE
        }

        if (isDateHeaderNeeded) {
            binding.receiveMessageDate.visibility = View.VISIBLE
            binding.leftLine.visibility = View.VISIBLE
            binding.rightLine.visibility = View.VISIBLE
            binding.receiveMessageDate.text = SimpleDateFormat("yyyy년 MM월 dd일").format(message.time.toLong())
        } else {
            binding.receiveMessageDate.visibility = View.GONE
            binding.leftLine.visibility = View.GONE
            binding.rightLine.visibility = View.GONE
        }
        binding.messageImage.setOnClickListener {
            val action = MessageFragmentDirections.actionFragmentMessageToFragmentProfile(user)
            itemView.findNavController().navigate(action)
        }
    }
}