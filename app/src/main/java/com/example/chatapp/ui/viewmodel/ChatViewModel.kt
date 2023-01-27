package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.ui.adapter.UserListAdapter
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository): ViewModel() {

    private val _current = repository.current
    val current: LiveData<ArrayList<User>>
        get() = _current

    fun getUserData() = viewModelScope.launch {
        repository.getUserData()
    }

    fun logout(){
        repository.logout()
    }

    fun sendMessage(message:String, receiverUid:String, time:String) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time)
    }

}