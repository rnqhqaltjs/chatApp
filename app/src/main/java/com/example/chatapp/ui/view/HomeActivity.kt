package com.example.chatapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chatapp.R
import com.example.chatapp.data.repository.ChatRepositoryImpl
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.example.chatapp.ui.viewmodel.ChatViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        dbref = Firebase.database.reference

        setupJetpackNavigation()

        val chatRepository = ChatRepositoryImpl(application,auth,dbref)
        val factory = ChatViewModelProviderFactory(chatRepository)
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.itemsearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }

}