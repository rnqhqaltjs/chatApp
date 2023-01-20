package com.example.chatapp.data.repository

import androidx.lifecycle.LiveData
import com.example.chatapp.data.model.User

interface ChatRepository {
    val current: LiveData<ArrayList<User>>
    suspend fun getUserData()
}