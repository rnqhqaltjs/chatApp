package com.example.chatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.DateheaderItemBinding
import com.example.chatapp.databinding.ReceivemessageItemBinding
import com.example.chatapp.databinding.SendmessageItemBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class MessageListAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback) {

    private val receive = 1 //받는 타입
    private val send = 2 //보내는 타입
    private val date = 3 //날짜 타입

    // Grouped messages
    private val groupedMessages = mutableMapOf<String, List<Message>>()

    override fun submitList(list: List<Message>?) {
        // Group messages by date
        if (list != null) {
            groupedMessages.clear()
            for (message in list) {
                val date = message.time.split(" ")[0]
                val messagesForDate = groupedMessages[date]?.toMutableList() ?: mutableListOf()
                messagesForDate.add(message)
                groupedMessages[date] = messagesForDate
            }
        }
        super.submitList(list)
    }

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
            date -> { // 날짜 헤더를 위한 ViewHolder 추가
                val binding = DateheaderItemBinding.inflate(layoutInflater, parent, false)
                DateHeaderViewHolder(binding)
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
            date -> (holder as DateHeaderViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when {
            isDateHeaderNeeded(position) -> date // 해당 포지션에 날짜 헤더가 필요한 경우
            FirebaseAuth.getInstance().currentUser?.uid.equals(item.sendId) -> send
            else -> receive
        }
    }

    private fun isDateHeaderNeeded(position: Int): Boolean {
        if (position == 0) return true

        val currentMessage = getItem(position - 1)
        val previousMessage = getItem(position)

        // 현재 메시지와 이전 메시지가 같은 날에 작성된 것인지 확인
        val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(currentMessage.time.toLong()))
        val previousDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(previousMessage.time.toLong()))

        return currentDate != previousDate
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