package com.example.chatapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _friendRequestObserver = MutableLiveData<String>()
    val friendRequestObserver: LiveData<String>
        get() = _friendRequestObserver

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String>
        get() = _currentTime

    init {
        viewModelScope.launch {
            while (true) {
                _currentTime.value = System.currentTimeMillis().toString()
                delay(1000)
            }
        }
    }

    fun checkFriendRequestStatus(receiverUid: String) = viewModelScope.launch {
        repository.checkFriendRequestStatus(receiverUid){
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