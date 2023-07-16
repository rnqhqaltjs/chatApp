package com.example.chatapp.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.data.model.Request
import com.example.chatapp.databinding.FriendrequestItemBinding

class FriendRequestAdapter internal constructor() : ListAdapter<Request, FriendRequestViewHolder>(RequestDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        return FriendRequestViewHolder(
            FriendrequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    companion object {
        private val RequestDiffCallback = object : DiffUtil.ItemCallback<Request>() {
            override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
                return oldItem == newItem
            }
        }
    }
}