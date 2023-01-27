package com.example.chatapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val image: String,
    val uid: String
):Parcelable {
    constructor(): this("", "", "", "")
}
