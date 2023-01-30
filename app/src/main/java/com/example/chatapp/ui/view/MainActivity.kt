package com.example.chatapp.ui.view

import android.content.Context
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var storage: FirebaseStorage
    lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        dbref = Firebase.database.reference
        storage = Firebase.storage

        val authRepository = AuthRepositoryImpl(application,dataStore,auth,dbref,storage)
        val factory = AuthViewModelProviderFactory(authRepository)
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

    }
}