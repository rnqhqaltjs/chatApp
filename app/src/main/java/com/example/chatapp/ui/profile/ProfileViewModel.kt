package com.example.chatapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _friendRequestStatus = MutableLiveData<String>()
    val friendRequestStatus: LiveData<String>
        get() = _friendRequestStatus

    private val _requestNotificationLiveData = MutableLiveData<UiState<String>>()
    val requestNotificationLiveData: LiveData<UiState<String>>
        get() = _requestNotificationLiveData

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
            _friendRequestStatus.postValue(it)
        }
    }

    fun friendRequest(receiverUid: String, time: String) = viewModelScope.launch {
        repository.friendRequest(receiverUid, time){
            _friendRequestStatus.postValue(it)
        }
    }

    fun requestCancel(receiverUid: String) = viewModelScope.launch {
        repository.requestCancel(receiverUid){
            _friendRequestStatus.postValue(it)
        }
    }

    fun friendRequestNotification(message:String, userReceiver: User) = viewModelScope.launch {
        repository.friendRequestNotification(message, userReceiver){
            _requestNotificationLiveData.postValue(it)
        }
    }
}