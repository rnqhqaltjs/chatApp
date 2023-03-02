package com.example.chatapp.ui.viewmodel

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

    private val _userobserve = MutableLiveData<UiState<List<User>>>()
    val userobserve: LiveData<UiState<List<User>>>
        get() = _userobserve

    private val _messageobserve = MutableLiveData<UiState<List<Message>>>()
    val messageobserve: LiveData<UiState<List<Message>>>
        get() = _messageobserve

    private val _chatobserve = repository.currentchatadd
    val chatobserve: LiveData<ArrayList<Chat>>
        get() = _chatobserve

    private val _profileobserve = MutableLiveData<UiState<String>>()
    val profileobserve: LiveData<UiState<String>>
        get() = _profileobserve

    fun getUserData() = viewModelScope.launch {
        _userobserve.value = UiState.Loading
        repository.getUserData {
            _userobserve.value = it
        }
    }

    fun logout(){
        repository.logout()
    }

    fun sendMessage(message:String, receiverUid:String, time: String) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time)
    }

    fun getMessageData(receiverUid:String) = viewModelScope.launch {
        _messageobserve.value = UiState.Loading
        repository.getMessageData(receiverUid){
            _messageobserve.value = it
        }
    }

    fun getChatData() = viewModelScope.launch {
        repository.getChatData()
    }

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit)) = viewModelScope.launch {
        repository.getProfileData(image, name)
    }

    fun profileChange(name: String, image: ByteArray?) = viewModelScope.launch {
        _profileobserve.value = UiState.Loading
        repository.profileChange(name, image){
            _profileobserve.value = it
        }
    }

}