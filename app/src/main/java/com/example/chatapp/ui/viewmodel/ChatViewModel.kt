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

    private val _currentuseradd = MutableLiveData<UiState<List<User>>>()
    val currentuseradd: LiveData<UiState<List<User>>>
        get() = _currentuseradd

    private val _currentmessageadd = repository.currentmessageadd
    val currentmessageadd: LiveData<ArrayList<Message>>
        get() = _currentmessageadd

    private val _currentchatadd = repository.currentchatadd
    val currentchatadd: LiveData<ArrayList<Chat>>
        get() = _currentchatadd

    fun getUserData() = viewModelScope.launch {
        _currentuseradd.value = UiState.Loading
        repository.getUserData {
            _currentuseradd.value = it
        }
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

    fun profileImageChange(image: ByteArray) = viewModelScope.launch {
        repository.profileImageChange(image)
    }

    fun profileNameChange(name: String) = viewModelScope.launch {
        repository.profileNameChange(name)
    }

}