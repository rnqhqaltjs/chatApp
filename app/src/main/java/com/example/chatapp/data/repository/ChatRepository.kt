package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState

interface ChatRepository {
    val currentchatadd: LiveData<ArrayList<Chat>>
    fun logout()
    suspend fun getUserData(result: (UiState<List<User>>) -> Unit)
    suspend fun sendMessage(message:String, receiverUid:String, time:String)
    suspend fun getMessageData(receiverUid:String, result: (UiState<List<Message>>) -> Unit)
    suspend fun getChatData()
    suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit))
    suspend fun profileChange(name:String, image: ByteArray?, result: (UiState<String>)->Unit)
}