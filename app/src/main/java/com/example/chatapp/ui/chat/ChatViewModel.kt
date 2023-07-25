package com.example.chatapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
    ): ViewModel() {

    private val _chatDataList = MutableLiveData<UiState<List<Chat>>>()
    val chatDataList: LiveData<UiState<List<Chat>>>
        get() = _chatDataList

    fun getChatData() = viewModelScope.launch {
        _chatDataList.value = UiState.Loading
        repository.getChatData {
            _chatDataList.value = it
        }
    }
}