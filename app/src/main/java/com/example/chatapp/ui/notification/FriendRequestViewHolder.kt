package com.example.chatapp.ui.notification

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Request
import com.example.chatapp.databinding.FriendrequestItemBinding

class FriendRequestViewHolder(
    private val binding: FriendrequestItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notificationViewModel: NotificationViewModel, request: Request) {
        binding.viewmodel = notificationViewModel
        binding.request = request
        binding.executePendingBindings()
    }
}