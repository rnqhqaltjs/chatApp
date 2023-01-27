package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getUserData() = viewModelScope.launch {
        repository.getUserData()
    }

    fun logout(){
        repository.logout()
    }

    fun sendMessage(message:String, receiverUid:String, time: String) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time)
    }

    fun getMessageData(receiverUid:String) = viewModelScope.launch {
        repository.getMessageData(receiverUid)
    }

}