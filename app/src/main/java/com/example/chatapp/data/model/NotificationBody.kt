package com.example.chatapp.data.model

data class NotificationBody(
    val to: String,
    val data: NotificationData
) {
    data class NotificationData(
        val user: User,
        val senderUid: String,
        val message: String,
        val type: Int
    )
}