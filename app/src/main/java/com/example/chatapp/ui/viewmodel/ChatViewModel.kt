package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository): ViewModel() {

    private val _currentuseradd = repository.currentuseradd
    val currentuseradd: LiveData<ArrayList<User>>
        get() = _currentuseradd

    private val _currentmessageadd = repository.currentmessageadd
    val currentmessageadd: LiveData<ArrayList<Message>>
        get() = _currentmessageadd

    private val _currentchatadd = repository.currentchatadd
    val currentchatadd: LiveData<ArrayList<Chat>>
        get() = _currentchatadd

    fun getUserData() = viewModelScope.launch {
        repository.getUserData()
    }

    fun logout(){
        repository.logout()
    }

    fun sendMessage(message:String, receiverUid:String, time: String, image: String) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time, image)
    }

    fun getMessageData(receiverUid:String) = viewModelScope.launch {
        repository.getMessageData(receiverUid)
    }

    fun getChatData() = viewModelScope.launch {
        repository.getChatData()
    }

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit)) = viewModelScope.launch {
        repository.getProfileData(image, name)
    }

}