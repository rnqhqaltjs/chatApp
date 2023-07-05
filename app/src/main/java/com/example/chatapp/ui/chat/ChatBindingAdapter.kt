package com.example.chatapp.ui.chat

import android.graphics.Color
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.chatapp.data.model.Chat
import com.example.chatapp.util.getLastMessageTimeString
import com.google.firebase.auth.FirebaseAuth

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    view.load(url)
}

@BindingAdapter("lastMessageTime")
fun getLastMessageTimeString(view: TextView, time : String) {
    view.text = getLastMessageTimeString(time.toLong())
}

@BindingAdapter("chatMessageStyle")
fun setChatMessageStyle(view: TextView, chat: Chat){
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (chat.message.sendId != currentUser?.uid && !chat.message.seen) {
        view.setTextColor(Color.parseColor("#99CCFF"))
        view.setTypeface(null, Typeface.BOLD)
    }
}