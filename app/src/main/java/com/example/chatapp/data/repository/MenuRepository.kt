package com.example.chatapp.data.repository

import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState

interface MenuRepository {

    fun logout()
    suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit), result: (UiState<String>) -> Unit)
    suspend fun profileChange(name:String, image: ByteArray?, result: (UiState<String>)->Unit)
    suspend fun getUserSearchData(query: String, result: (UiState<List<User>>) -> Unit)

}