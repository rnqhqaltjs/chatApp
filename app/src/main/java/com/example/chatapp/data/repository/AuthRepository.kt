package com.example.chatapp.data.repository

interface AuthRepository {
    fun registerUser(email: String, password: String)
}