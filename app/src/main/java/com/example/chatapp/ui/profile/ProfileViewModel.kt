package com.example.chatapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _friendRequestObserver = MutableLiveData<String>()
    val friendRequestObserver: LiveData<String>
        get() = _friendRequestObserver

    fun getRequest(receiverUid: String) = viewModelScope.launch {
        repository.getRequest(receiverUid){
            _friendRequestObserver.postValue(it)
        }
    }

    fun friendRequest(receiverUid: String, time: String) = viewModelScope.launch {
        repository.friendRequest(receiverUid, time){
            _friendRequestObserver.postValue(it)
        }
    }

    fun requestCancel(receiverUid: String) = viewModelScope.launch {
        repository.requestCancel(receiverUid){
            _friendRequestObserver.postValue(it)
        }
    }

}