package com.example.chatapp.data.repository

import com.example.chatapp.data.model.User
import com.example.chatapp.ui.adapter.UserListAdapter

interface ChatRepository {
    suspend fun getUserData(userList: ArrayList<User>, adapter: UserListAdapter)
}