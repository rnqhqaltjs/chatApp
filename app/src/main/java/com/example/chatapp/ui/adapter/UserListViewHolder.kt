package com.example.chatapp.ui.adapter

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.R
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding

class UserListViewHolder(
    private val binding: UserlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        itemView.apply {
            binding.userImage.load(user.image)
            binding.userName.text = user.name
            binding.userEmail.text = user.email
        }
    }
}