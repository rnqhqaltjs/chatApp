package com.example.chatapp.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit)) = viewModelScope.launch {
        repository.getProfileData(image, name, email)
    }

    fun logout(){
        repository.logout()
    }
}