package com.example.chatapp.data.repository

import com.example.chatapp.util.UiState

interface AuthRepository {
    suspend fun login(email: String, password: String, result: (UiState<String>) -> Unit)
    suspend fun signup(name: String, email: String, image: ByteArray, password: String, result: (UiState<String>) -> Unit)
    suspend fun putID(key: String,value: String)
    suspend fun getID(key: String):String?
}