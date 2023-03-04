package com.example.chatapp.data.model

data class Chat(
//    val users: HashMap<String, User>? = HashMap(),
//    var messages: HashMap<String,Message>? = HashMap()
    var message: Message = Message("", "", "", ""),
    var user: User = User("", "", "", "")
)