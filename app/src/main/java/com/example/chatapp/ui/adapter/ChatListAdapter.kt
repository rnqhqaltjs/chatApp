package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.chatapp.data.model.Chat
import com.example.chatapp.databinding.ChatlistItemBinding

//class ChatListAdapter : ListAdapter<Chat.Comment, ChatListViewHolder>(ChatDiffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
//        return ChatListViewHolder(
//            ChatlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//    }
//
//    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
//        holder.bind(currentList[position])
//    }
//
//    companion object {
//        private val ChatDiffCallback = object : DiffUtil.ItemCallback<Chat.Comment>() {
//            override fun areItemsTheSame(oldItem: Chat.Comment, newItem: Chat.Comment): Boolean {
//                return oldItem.uid == newItem.uid
//            }
//
//            override fun areContentsTheSame(oldItem: Chat.Comment, newItem: Chat.Comment): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}
class ChatListAdapter {

}
