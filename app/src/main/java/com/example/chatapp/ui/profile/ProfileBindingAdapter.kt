package com.example.chatapp.ui.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.chatapp.data.model.User

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    view.load(url)
}

@BindingAdapter("onProfileMessageClicked")
fun setOnProfileMessageClicked(view: ImageView, user: User) {
    view.setOnClickListener {
        val action = ProfileFragmentDirections.actionFragmentProfileToFragmentMessage(user)
        view.findNavController().navigate(action)
    }
}
