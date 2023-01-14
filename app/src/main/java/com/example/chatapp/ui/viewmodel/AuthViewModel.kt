package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private var repository: AuthRepository) : ViewModel() {

    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    fun register(email: String, password: String) {
        repository.registerUser(email, password)
    }

}