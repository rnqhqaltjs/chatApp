package com.example.chatapp.data.model

data class Chat (
    val users: HashMap<String, Boolean> = HashMap(),
    val comments : HashMap<String, Comment> = HashMap()
){
    data class Comment(
        val uid: String? = "",
        val message: String? = "",
        val time: String? = ""
    )
}