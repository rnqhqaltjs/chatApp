package com.example.chatapp.ui.message

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.chatapp.data.model.User
import java.text.SimpleDateFormat

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    view.load(url)
}

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