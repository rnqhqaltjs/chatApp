package com.example.chatapp.ui.register

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
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerLiveData = MutableLiveData<UiState<String>>()
    val registerLiveData: LiveData<UiState<String>>
        get() = _registerLiveData

    fun register(name:String, email: String, image: ByteArray, password: String) = viewModelScope.launch {
        _registerLiveData.value = UiState.Loading
        repository.registerUser(name, email, image, password){
            _registerLiveData.value = it
        }
    }

}