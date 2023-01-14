package com.example.chatapp.data.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository() {
    private lateinit var  application: Application
    private val auth  = FirebaseAuth.getInstance()

    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    val login: LiveData<FirebaseUser>
        get() = _login

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _register.postValue(auth.currentUser)

                } else {
                    Toast.makeText(application,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }
    }

    fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _login.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application,"로그인 실패",Toast.LENGTH_SHORT).show()
                 }
             }
    }

    fun logout(){
        auth.signOut()
    }


}