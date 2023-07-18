package com.example.chatapp.ui.findfriend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.UserlistItemBinding
import com.example.chatapp.databinding.UsersearchItemBinding

class UserSearchAdapter internal constructor()
    : ListAdapter<User, UserSearchViewHolder>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
        return UserSearchViewHolder(
            UsersearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(item) }
        }
    }

    private var onItemClickListener: ((User) -> Unit)? = null
    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
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