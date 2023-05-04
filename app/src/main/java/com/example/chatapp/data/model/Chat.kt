package com.example.chatapp.data.model

data class Chat(
    var message: Message,
    var user: User
){
    constructor(): this(
        Message("","","","",false),
        User("","","","", "")
    )
}