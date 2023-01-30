package com.example.chatapp.data.model

data class Chat(
    var name: String,
    var sendId: String,
    var lastmessage: String,
    var time: String,
    var image: String
) {
    constructor(): this("", "", "", "", "")
}