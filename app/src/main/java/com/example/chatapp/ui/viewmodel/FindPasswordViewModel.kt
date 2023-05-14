package com.example.chatapp.ui.viewmodel

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

    private val _findpassobserve = MutableLiveData<UiState<String>>()
    val findpassobserve: LiveData<UiState<String>>
        get() = _findpassobserve

    fun findPassword(email: String) = viewModelScope.launch {
        _findpassobserve.value = UiState.Loading
        repository.findPassword(email) {
            _findpassobserve.value = it
        }
    }
}