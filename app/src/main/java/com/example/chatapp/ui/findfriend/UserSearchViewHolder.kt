package com.example.chatapp.ui.findfriend

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding
import com.example.chatapp.databinding.UsersearchItemBinding

class UserSearchViewHolder(
    private val binding: UsersearchItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.userEmail.text = user.email
        binding.userImage.load(user.image)
        binding.userName.text = user.name

    }
}