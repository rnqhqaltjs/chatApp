package com.example.chatapp.ui.notification

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Request
import com.example.chatapp.databinding.FriendrequestItemBinding

class FriendRequestViewHolder(
    private val binding: FriendrequestItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notificationViewModel: NotificationViewModel, request: Request) {
        binding.requestName.text = request.name
        binding.requestImage.load(request.image)

        binding.requestDeclineBtn.setOnClickListener {
            notificationViewModel.declineRequest(request.uid)
        }

        binding.requestAcceptBtn.setOnClickListener {
            notificationViewModel.acceptRequest(request.uid)
        }
    }
}