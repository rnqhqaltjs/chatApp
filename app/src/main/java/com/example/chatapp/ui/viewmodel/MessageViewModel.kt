package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _messageobserve = MutableLiveData<UiState<List<Message>>>()
    val messageobserve: LiveData<UiState<List<Message>>>
        get() = _messageobserve

    fun sendMessage(message:String, receiverUid:String, time: String, userReceiver: User) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time, userReceiver)
    }

    fun getMessageData(receiverUid:String) = viewModelScope.launch {
        _messageobserve.value = UiState.Loading
        repository.getMessageData(receiverUid){
            _messageobserve.value = it
        }
    }

}