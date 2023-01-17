package com.example.chatapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    val login: LiveData<FirebaseUser>
        get() = _login

    fun register(email: String, password: String) {
        repository.registerUser(email, password)
    }

    fun login(email: String, password: String) {
        repository.loginUser(email, password)
    }

    fun logout(){
        repository.logout()
    }

//    fun putID(value: String) = viewModelScope.launch(Dispatchers.IO) {
//        repository.putID(value,"key")
//
//    }
//
//    fun getID() = runBlocking {
//        repository.getID("key")
//    }

}