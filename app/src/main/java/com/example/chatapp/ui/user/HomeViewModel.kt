package com.example.chatapp.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _userDataList = MutableLiveData<UiState<List<User>>>()
    val userDataList: LiveData<UiState<List<User>>>
        get() = _userDataList

    fun getFriendsData() = viewModelScope.launch {
        _userDataList.value = UiState.Loading
        repository.getFriendsData {
            _userDataList.value = it
        }
    }

    fun removeFriend(receiverUid: String) = viewModelScope.launch {
        repository.removeFriend(receiverUid)
    }
}