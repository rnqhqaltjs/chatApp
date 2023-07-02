package com.example.chatapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val authViewModel by viewModels<LoginViewModel>()

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        startSomeNextActivity()
    }

    private fun startSomeNextActivity() {
        lifecycleScope.launchWhenStarted {
            val isLoggedIn = auth?.currentUser != null && authViewModel.getLoginBox()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = if (isLoggedIn) {
                    Intent(this@StartActivity, HomeActivity::class.java)
                } else {
                    Intent(this@StartActivity, MainActivity::class.java)
                }
                startActivity(intent)
                finish()
            }, 500)
        }
    }
}