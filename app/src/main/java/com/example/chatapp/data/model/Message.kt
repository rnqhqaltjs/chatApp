package com.example.chatapp.data.model

data class Message(
    var message: String,
    var sendId: String,
    var time: String,
    var image: String,
    val seen: Boolean
) {
    constructor(): this("", "", "", "",false)
}