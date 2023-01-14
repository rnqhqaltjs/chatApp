package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl(): AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    override fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _register.postValue(auth.currentUser)

                } else {

                }
            }
    }
}