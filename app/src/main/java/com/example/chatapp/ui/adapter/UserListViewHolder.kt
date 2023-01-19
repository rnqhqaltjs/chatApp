package com.example.chatapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding

class UserListViewHolder(
    private val binding: UserlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {

        itemView.apply {
            binding.userName.text = user.name
            binding.userEmail.text = user.email
        }
    }
}