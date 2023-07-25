package com.example.chatapp.ui.findpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _passwordResetLiveData = MutableLiveData<UiState<String>>()
    val passwordResetLiveData: LiveData<UiState<String>>
        get() = _passwordResetLiveData

    fun findPassword(email: String) = viewModelScope.launch {
        _passwordResetLiveData.value = UiState.Loading
        repository.findPassword(email) {
            _passwordResetLiveData.value = it
        }
    }
}