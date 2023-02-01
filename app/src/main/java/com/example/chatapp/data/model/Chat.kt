package com.example.chatapp.data.model

data class Chat(
    val users: HashMap<String, Boolean>? = HashMap(),
    var messages: HashMap<String,Message>? = HashMap()
)