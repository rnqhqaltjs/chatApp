package com.example.chatapp.data.model

data class Message(
    var message: String?,
    var sendId: String?,
    var time: String?
){
    constructor():this("","","")
}