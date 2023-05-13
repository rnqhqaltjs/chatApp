package com.example.chatapp

import android.app.Application
import android.content.Context
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        toast?.cancel()
        toast = Toast.makeText(context, message, duration)
        toast?.show()
    }
}