package com.example.chatapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _register = MutableLiveData<User<String>>()
    val register: LiveData<User<String>>
        get() = _register

    fun register(email: String, password: String) {
        repository.registerUser(email, password)
    }

}