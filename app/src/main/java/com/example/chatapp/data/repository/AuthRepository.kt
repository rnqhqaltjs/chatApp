package com.example.chatapp.data.repository

import com.example.chatapp.util.UiState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    suspend fun registerUser(name: String, email: String, image: ByteArray, password: String, result: (UiState<String>) -> Unit)
    suspend fun findPassword(email: String, result: (UiState<String>) -> Unit)
    suspend fun putID(key: String,value: String)
    suspend fun getID(key: String): Flow<String>
    suspend fun saveLoginBox(key: String, value: Boolean)
    suspend fun getLoginBox(key: String): Flow<Boolean>
}