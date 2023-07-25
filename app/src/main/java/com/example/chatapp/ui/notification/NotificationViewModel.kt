package com.example.chatapp.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Request
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _friendRequestDataList = MutableLiveData<UiState<List<Request>>>()
    val friendRequestDataList: LiveData<UiState<List<Request>>>
        get() = _friendRequestDataList

    fun getRequestData() = viewModelScope.launch {
        _friendRequestDataList.value = UiState.Loading
        repository.getRequestData {
            _friendRequestDataList.value = it
        }
    }

    fun declineRequest(receiverUid: String) = viewModelScope.launch {
        repository.declineRequest(receiverUid)
    }

    fun acceptRequest(receiverUid: String) = viewModelScope.launch {
        repository.acceptRequest(receiverUid)
    }
}