package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String, result: (UiState<String>) -> Unit)
    suspend fun signup(name: String, email: String, image: ByteArray, password: String)
    suspend fun putID(key: String,value: String)
    suspend fun getID(key: String):String?
}