package com.example.chatapp.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryImpl(private val auth: FirebaseAuth): AuthRepository {

    override fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
            }
    }
}