package com.example.chatapp.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.SingleLiveEvent
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _profileobserve = MutableLiveData<UiState<String>>()
    val profileobserve: LiveData<UiState<String>>
        get() = _profileobserve

    private val _profilechangeobserve = SingleLiveEvent<UiState<String>>()
    val profilechangeobserve: LiveData<UiState<String>>
        get() = _profilechangeobserve

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit)) = viewModelScope.launch {
        _profileobserve.value = UiState.Loading
        repository.getProfileData(image, name, email){
            _profileobserve.value = it
        }
    }

    fun profileChange(name: String, image: ByteArray?) = viewModelScope.launch {
        _profilechangeobserve.value = UiState.Loading
        repository.profileChange(name, image){
            _profilechangeobserve.value = it
        }
    }

}