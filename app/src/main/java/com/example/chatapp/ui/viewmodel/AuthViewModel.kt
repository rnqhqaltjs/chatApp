package com.example.chatapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.example.chatapp.util.Constants.USER_NAME
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _register = repository.register
    val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = repository.login
    val login: LiveData<FirebaseUser>
        get() = _login


    fun register(name:String, email: String, password: String) = viewModelScope.launch {
        repository.signup(name, email, password)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        repository.login(email, password)
    }

    fun logout(){
        repository.logout()
    }

    fun putID(value:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.putID(USER_NAME, value)
        }
    }

    fun getID():String? = runBlocking {
            repository.getID(USER_NAME)
         }

}