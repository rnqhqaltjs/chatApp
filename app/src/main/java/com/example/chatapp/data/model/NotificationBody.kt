package com.example.chatapp.data.model

data class NotificationBody(
    val to: String,
    val data: NotificationData
) {
    data class NotificationData(
        val name: String,
        val message: String,
        val image: String
    )
}