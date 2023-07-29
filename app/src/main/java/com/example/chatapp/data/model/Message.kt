package com.example.chatapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var message: String,
    var sendId: String,
    var time: String,
    var profileImage: String,
    var photoImage: String,
    val seen: Boolean
): Parcelable {
    constructor(): this("", "", "", "", "",false)
}