package com.example.chatapp.ui.findfriend

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
class FindFriendViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _userSearch = MutableLiveData<UiState<List<User>>>()
    val userSearch: LiveData<UiState<List<User>>>
        get() = _userSearch

    fun getUserSearchData(query: String) = viewModelScope.launch {
        _userSearch.value = UiState.Loading
        repository.getUserSearchData(query) {
            _userSearch.value = it
        }
    }
}