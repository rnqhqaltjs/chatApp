package com.example.chatapp.ui.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chatapp.data.model.Message
import com.example.chatapp.databinding.ReceivemessageItemBinding
import java.text.SimpleDateFormat

class ReceiveMessageViewHolder(
    private val binding: ReceivemessageItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SimpleDateFormat")
    fun bind(message: Message) {
        itemView.apply {
            binding.receiveMessageText.text = message.message
            binding.receiveMessageTime.text = SimpleDateFormat("hh:mm a").format(message.time.toLong())
            binding.messageImage.load(message.image)
        }
    }

    // Function to check if two times have the same minute
//    private fun isSameMinute(time1: String, time2: String): Boolean {
//        val lastTimeString = SimpleDateFormat("yyyyMMddHHmmss").format(time1.toLong())
//        val newTimeString = SimpleDateFormat("yyyyMMddHHmmss").format(time2.toLong())
//        val lastHour = lastTimeString.substring(8, 10).toInt()
//        val lastMinute = lastTimeString.substring(10, 12).toInt()
//
//        val newHour = newTimeString.substring(8, 10).toInt()
//        val newMinute = newTimeString.substring(10, 12).toInt()
//        return lastHour == newHour && lastMinute == newMinute
//    }
}