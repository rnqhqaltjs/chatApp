package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.ReceivemessageItemBinding
import com.example.chatapp.databinding.SendmessageItemBinding
import com.google.firebase.auth.FirebaseAuth

class MessageListAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback) {

    private val receive = 1 //받는 타입
    private val send = 2 //보내는 타입

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            send -> {
                val binding = SendmessageItemBinding.inflate(layoutInflater, parent, false)
                SendMessageViewHolder(binding)
            }
            receive -> {
                val binding = ReceivemessageItemBinding.inflate(layoutInflater, parent, false)
                ReceiveMessageViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            send -> (holder as SendMessageViewHolder).bind(getItem(position))
            receive -> (holder as ReceiveMessageViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(getItem(position).sendId)) {
            send
        } else {
            receive
        }
    }

    companion object {
        private val MessageDiffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}