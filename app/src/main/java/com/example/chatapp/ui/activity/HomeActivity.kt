package com.example.chatapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.ui.message.MessageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView
    private val activityViewModel: ActivityViewModel by viewModels()

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupJetpackNavigation()
        showRequestCountBadge()
        showUnreadMessageBadge()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_message ||
                destination.id == R.id.fragment_profile ||
                destination.id == R.id.fragment_edit_profile ||
                destination.id == R.id.fragment_find_friend
            ) {
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
            setOf(R.id.fragment_user, R.id.fragment_notification, R.id.fragment_chat, R.id.fragment_menu)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun showUnreadMessageBadge() {
        bottomNavigationView = binding.bottomNavigationView
        val badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.fragment_chat)
        badgeDrawable.backgroundColor = ContextCompat.getColor(this, R.color.orange)
        badgeDrawable.horizontalOffset = 10
        badgeDrawable.verticalOffset = 10
        activityViewModel.getNonSeenData { count ->
            if (count > 0) {
                badgeDrawable.isVisible =true
                badgeDrawable.number = count
            } else {
                badgeDrawable.isVisible = false
            }
        }
    }

    private fun showRequestCountBadge() {
        bottomNavigationView = binding.bottomNavigationView
        val badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.fragment_notification)
        badgeDrawable.backgroundColor = ContextCompat.getColor(this, R.color.orange)
        badgeDrawable.horizontalOffset = 10
        badgeDrawable.verticalOffset = 10
        activityViewModel.getRequestCount { count ->
            if (count > 0) {
                badgeDrawable.isVisible =true
                badgeDrawable.number = count
            } else {
                badgeDrawable.isVisible = false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}