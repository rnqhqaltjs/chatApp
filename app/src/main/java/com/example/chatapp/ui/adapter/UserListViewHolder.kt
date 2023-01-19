package com.example.chatapp.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding

class UserListViewHolder(
    private val binding: UserlistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {

        itemView.apply {
            binding.ivArticleImage.load(item.image)
            binding.tvTitle.text = Html.fromHtml(title).toString()
            binding.tvLprice.text = item.lprice.toString() + "원"
            binding.tvCategory1.text = item.category1
        }
    }
}