package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User

interface ChatRepository {
    val currentuseradd: LiveData<ArrayList<User>>
    val currentmessageadd: LiveData<ArrayList<Message>>
    val currentchatadd: LiveData<ArrayList<Chat>>
    fun logout()
    suspend fun getUserData()
    suspend fun sendMessage(message:String, receiverUid:String, time:String, image:String)
    suspend fun getMessageData(receiverUid:String)
    suspend fun getChatData()
    suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit))
    suspend fun profileImageChange(image: ByteArray)
    suspend fun profileNameChange(name: String)
}