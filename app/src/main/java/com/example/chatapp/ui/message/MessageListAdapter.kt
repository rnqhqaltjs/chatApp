package com.example.chatapp.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.ReceiveimageItemBinding
import com.example.chatapp.databinding.ReceivemessageItemBinding
import com.example.chatapp.databinding.SendimageItemBinding
import com.example.chatapp.databinding.SendmessageItemBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MessageListAdapter internal constructor(private val viewModel: MessageViewModel, private val user: User)
    : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback) {

    private val send = 1
    private val sendImage = 2
    private val receive = 3
    private val receiveImage = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            send -> {
                val binding = SendmessageItemBinding.inflate(layoutInflater, parent, false)
                SendMessageViewHolder(binding)
            }
            sendImage -> {
                val binding = SendimageItemBinding.inflate(layoutInflater, parent, false)
                SendImageViewHolder(binding)
            }
            receive -> {
                val binding = ReceivemessageItemBinding.inflate(layoutInflater, parent, false)
                ReceiveMessageViewHolder(binding)
            }

            receiveImage -> {
                val binding = ReceiveimageItemBinding.inflate(layoutInflater, parent, false)
                ReceiveImageViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            send -> (holder as SendMessageViewHolder).bind(viewModel, getItem(position), isFirstDate(position), isFirstTime(position))
            sendImage -> (holder as SendImageViewHolder).bind(viewModel, getItem(position), isFirstDate(position), isFirstTime(position))
            receive -> (holder as ReceiveMessageViewHolder).bind(viewModel, getItem(position), isFirstDate(position), isFirstTime(position), user)
            receiveImage -> (holder as ReceiveImageViewHolder).bind(viewModel, getItem(position), isFirstDate(position), isFirstTime(position), user)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (FirebaseAuth.getInstance().currentUser?.uid == item.sendId) {
            when {
                item.photoImage != "" -> sendImage
                else -> send
            }
        } else {
            when {
                item.photoImage != "" -> receiveImage
                else -> receive
            }
        }
    }

    private fun isFirstDate(position: Int): Boolean {
        if (position == 0) return true

        val currentMessage = getItem(position)
        val previousMessage = getItem(position - 1)

        // 현재 메시지와 이전 메시지가 같은 날에 작성된 것인지 확인
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(currentMessage.time.toLong()))
        val previousDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(previousMessage.time.toLong()))

        return currentDate != previousDate
    }

    private fun isFirstTime(position: Int): Boolean {
        if (position == 0) return true

        val currentMessage = getItem(position)
        val previousMessage = getItem(position - 1)

        // 현재 메시지와 이전 메시지의 년월일시분이 모두 같은지 확인
        val currentDateTime = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(Date(currentMessage.time.toLong()))
        val previousDateTime = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault()).format(Date(previousMessage.time.toLong()))

        return currentDateTime != previousDateTime
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