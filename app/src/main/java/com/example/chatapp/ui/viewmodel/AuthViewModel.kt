package com.example.chatapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.util.Constants.USER_NAME
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
    ) : ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun register(name:String, email: String, image: ByteArray, password: String) = viewModelScope.launch {
        _register.value = UiState.Loading
        repository.registerUser(name, email, image, password){
            _register.value = it
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _login.value = UiState.Loading
        repository.loginUser(email, password){
            _login.value = it
        }
    }

    fun putID(value:String) = viewModelScope.launch(Dispatchers.IO) {
        repository.putID(USER_NAME, value)
    }

    fun getID() = runBlocking {
        repository.getID(USER_NAME)
    }

}