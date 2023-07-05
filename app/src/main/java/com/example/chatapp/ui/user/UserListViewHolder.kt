package com.example.chatapp.ui.user

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding

class UserListViewHolder(
    private val binding: UserlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: HomeViewModel, user: User) {
        binding.viewmodel = viewModel
        binding.user = user
        binding.executePendingBindings()
    }
}