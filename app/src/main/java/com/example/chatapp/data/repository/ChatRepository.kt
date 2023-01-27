package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User

interface ChatRepository {
    val currentuseradd: LiveData<ArrayList<User>>
    val currentmessageadd: LiveData<ArrayList<Message>>
    fun logout()
    suspend fun getUserData()
    suspend fun sendMessage(message:String, receiverUid:String, time:String)
    suspend fun getMessageData(receiverUid:String)
}