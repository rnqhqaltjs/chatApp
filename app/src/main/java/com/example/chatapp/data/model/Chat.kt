package com.example.chatapp.data.model

data class Chat(
    val name: String,
    val email: String,
    val image: String,
    val uid: String
){
    constructor(): this("", "", "", "")
}
