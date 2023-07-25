package com.example.chatapp.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding

class UserListAdapter internal constructor(private val viewModel: UserViewModel)
    : ListAdapter<User, UserListViewHolder>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(
            UserlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(viewModel, item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(item) }
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.let { it(item) }
            true
        }
    }

    private var onItemClickListener: ((User) -> Unit)? = null
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((User) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (User) -> Unit) {
        onItemLongClickListener = listener
    }

    companion object {
        private val UserDiffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}