package com.example.chatapp.data.repository

interface AuthRepository {

    fun registerUser(email: String, password: String)
    fun loginUser(email: String, password: String)
    fun logout()
//    suspend fun putID(key: String, value: String)
//    suspend fun getID(key: String)
}