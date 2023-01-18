package com.example.chatapp.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.data.repository.AuthRepositoryImpl
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel
import com.example.chatapp.ui.viewmodel.AuthViewModelProviderFactory
import com.example.chatapp.util.Constants.DATASTORE_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    lateinit var authViewModel: AuthViewModel
    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        val authRepository = AuthRepositoryImpl(application,dataStore,auth)
        val factory = AuthViewModelProviderFactory(authRepository)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

    }
}