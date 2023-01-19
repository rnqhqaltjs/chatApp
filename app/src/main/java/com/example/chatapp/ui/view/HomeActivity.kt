package com.example.chatapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chatapp.R
import com.example.chatapp.data.repository.AuthRepositoryImpl
import com.example.chatapp.data.repository.ChatRepositoryImpl
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel
import com.example.chatapp.ui.viewmodel.AuthViewModelProviderFactory
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.example.chatapp.ui.viewmodel.ChatViewModelProviderFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupJetpackNavigation()

        val chatRepository = ChatRepositoryImpl()
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