package com.example.chatapp.util

import android.graphics.Color
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.User
import com.example.chatapp.ui.message.MessageFragmentDirections
import com.example.chatapp.ui.profile.ProfileFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    view.load(url)
}

//chat
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

//message
@BindingAdapter("onProfileImageClicked")
fun setOnProfileImageClicked(view: ImageView, user: User) {
    view.setOnClickListener {
        val action = MessageFragmentDirections.actionFragmentMessageToFragmentProfile(user)
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("onTimeFormat")
fun setOnTimeFormat(view: TextView, time: String){
    view.text = SimpleDateFormat("hh:mm a").format(time.toLong())
}

@BindingAdapter("onDateFormat")
fun setOnDateFormat(view: TextView, time: String){
    view.text = SimpleDateFormat("yyyy년 MM월 dd일").format(time.toLong())
}

//profile
@BindingAdapter("onProfileMessageClicked")
fun setOnProfileMessageClicked(view: ImageView, user: User) {
    view.setOnClickListener {
        val action = ProfileFragmentDirections.actionFragmentProfileToFragmentMessage(user)
        view.findNavController().navigate(action)
    }
}
