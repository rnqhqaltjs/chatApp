package com.example.chatapp.data.repository

import android.app.Application
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.first

class AuthRepository() {
    private lateinit var  application: Application
    private val auth  = FirebaseAuth.getInstance()
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    val login: LiveData<FirebaseUser>
        get() = _login

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _register.postValue(auth.currentUser)

                } else {
                    Toast.makeText(application,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }
    }

    fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _login.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application,"로그인 실패",Toast.LENGTH_SHORT).show()
                 }
             }
    }

    fun logout(){
        auth.signOut()
    }

    suspend fun putID(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    suspend fun getID(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = dataStore.data.first()
            preferences[preferencesKey]

        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }


}