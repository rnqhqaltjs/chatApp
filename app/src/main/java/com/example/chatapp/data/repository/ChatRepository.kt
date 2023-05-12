package com.example.chatapp.data.repository

import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.NotificationBody
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState
import com.google.firebase.database.ValueEventListener

interface ChatRepository {
    fun logout()
    suspend fun getUserData(result: (UiState<List<User>>) -> Unit)
    suspend fun sendMessage(message:String, receiverUid:String, time:String, userReceiver: User)
    var MessageSeenListener: ValueEventListener?
    var LatestMessageSeenListener: ValueEventListener?
    fun seenMessage(receiverUid: String)
    suspend fun getMessageData(receiverUid:String, result: (UiState<List<Message>>) -> Unit)
    suspend fun getChatData(result: (UiState<List<Chat>>) -> Unit)
    suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit))
    suspend fun profileChange(name:String, image: ByteArray?, result: (UiState<String>)->Unit)
    suspend fun sendNotification(message:String, userReceiver: User, result: (UiState<String>) -> Unit)
    fun removeSeenMessage(receiverUid: String)
}