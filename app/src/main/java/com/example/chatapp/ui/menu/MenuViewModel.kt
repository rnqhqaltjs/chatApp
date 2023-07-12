package com.example.chatapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _profileobserve = MutableLiveData<UiState<String>>()
    val profileobserve: LiveData<UiState<String>>
        get() = _profileobserve

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit)) = viewModelScope.launch {
        _profileobserve.value = UiState.Loading
        repository.getProfileData(image, name, email){
            _profileobserve.value = it
        }
    }

    fun logout(){
        repository.logout()
    }
}