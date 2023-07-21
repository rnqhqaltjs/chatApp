package com.example.chatapp.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    fun getNonSeenData(count: ((Int)->Unit)) = viewModelScope.launch {
        chatRepository.getNonSeenData(count)
    }

    fun getRequestCount(count: ((Int)->Unit)) = viewModelScope.launch {
        chatRepository.getRequestCount(count)
    }

    suspend fun getLoginBox(): Boolean = withContext(Dispatchers.IO) {
        authRepository.getLoginBox(Constants.CHECK_BOX).first()
    }
}