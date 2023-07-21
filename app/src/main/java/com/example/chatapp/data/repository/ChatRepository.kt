package com.example.chatapp.data.repository

import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.Request
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState

interface ChatRepository {
    suspend fun getUserData(result: (UiState<List<User>>) -> Unit)
    suspend fun sendMessage(message:String, receiverUid:String, time:String, userReceiver: User)
    suspend fun sendImageMessage(message: String, image: ByteArray?, receiverUid:String, time:String, userReceiver: User, result: (UiState<String>) -> Unit)
    fun seenMessage(receiverUid: String)
    fun removeSeenMessage(receiverUid: String)
    suspend fun getMessageData(receiverUid:String, result: (UiState<List<Message>>) -> Unit)
    suspend fun getChatData(result: (UiState<List<Chat>>) -> Unit)
    suspend fun getNonSeenData(count: ((Int)->Unit))
    suspend fun sendNotification(message:String, userReceiver: User, result: (UiState<String>) -> Unit)
    suspend fun getRequest(receiverUid: String, result: (String)->Unit)
    suspend fun friendRequest(receiverUid: String, time: String, result: (String)->Unit)
    suspend fun requestCancel(receiverUid: String, result: (String)->Unit)
    suspend fun getRequestData(result: (UiState<List<Request>>) -> Unit)
    suspend fun declineRequest(receiverUid: String)
    suspend fun acceptRequest(receiverUid: String)
    suspend fun removeFriend(receiverUid: String)
    suspend fun getRequestCount(count: ((Int)->Unit))

}