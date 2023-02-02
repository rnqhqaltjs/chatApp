package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var storage: FirebaseStorage
    lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        dbref = Firebase.database.reference
        storage = Firebase.storage

        setupJetpackNavigation()

        val chatRepository = ChatRepositoryImpl(application,auth,dbref,storage)
        val factory = ChatViewModelProviderFactory(chatRepository)
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.fragment_message) {

                binding.bottomNavigationView.visibility = View.GONE
            } else {

                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }

    private fun setupJetpackNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.itemsearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragment_home, R.id.fragment_chat, R.id.fragment_settings)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}