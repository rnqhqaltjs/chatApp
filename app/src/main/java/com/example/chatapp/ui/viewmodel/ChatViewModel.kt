package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.ui.adapter.UserListAdapter
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository): ViewModel() {

    fun getUserData(userList: ArrayList<User>, adapter: UserListAdapter) = viewModelScope.launch {
        repository.getUserData(userList, adapter)
    }
}