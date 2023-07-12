package com.example.chatapp.data.repository

import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState

interface ChatRepository {
    fun logout()
    suspend fun getUserData(result: (UiState<List<User>>) -> Unit)
    suspend fun sendMessage(message:String, receiverUid:String, time:String, userReceiver: User)
    suspend fun sendImageMessage(message: String, image: ByteArray?, receiverUid:String, time:String, userReceiver: User, result: (UiState<String>) -> Unit)
    fun seenMessage(receiverUid: String)
    fun removeSeenMessage(receiverUid: String)
    suspend fun getMessageData(receiverUid:String, result: (UiState<List<Message>>) -> Unit)
    suspend fun getChatData(result: (UiState<List<Chat>>) -> Unit)
    suspend fun getNonSeenData(count: ((Int)->Unit))
    suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit))
    suspend fun profileChange(name:String, image: ByteArray?, result: (UiState<String>)->Unit)
    suspend fun sendNotification(message:String, userReceiver: User, result: (UiState<String>) -> Unit)

}