package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val register: LiveData<FirebaseUser>
    val login: LiveData<FirebaseUser>
    suspend fun login(email: String, password: String)
    suspend fun signup(name: String, email: String, image: ByteArray, password: String)
    suspend fun putID(key: String,value: String)
    suspend fun getID(key: String):String?
}