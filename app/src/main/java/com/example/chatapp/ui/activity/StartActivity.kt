package com.example.chatapp.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val authViewModel by viewModels<ActivityViewModel>()

    private var auth : FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        requestPermission()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                startSomeNextActivity()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    this@StartActivity,
                    "권한 거부\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            .check()
    }

    private fun startSomeNextActivity() {
        lifecycleScope.launch {
            val isLoggedIn = auth?.currentUser != null && authViewModel.getLoginBox()
            delay(500)
            val intent = if (isLoggedIn) {
                Intent(this@StartActivity, HomeActivity::class.java)
            } else {
                Intent(this@StartActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}