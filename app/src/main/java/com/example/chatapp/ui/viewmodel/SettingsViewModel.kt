package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.SingleLiveEvent
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _profileobserve = SingleLiveEvent<UiState<String>>()
    val profileobserve: LiveData<UiState<String>>
        get() = _profileobserve

    fun logout(){
        repository.logout()
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