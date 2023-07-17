package com.example.chatapp.data.model

data class Request (
    val name: String,
    val uid: String,
    val status: String,
    val time: String,
    val image: String
) {
    constructor(): this("", "", "", "", "")
}