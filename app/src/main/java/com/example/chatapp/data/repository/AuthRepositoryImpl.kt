package com.example.chatapp.data.repository

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl(
    private val application: Application,
//    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth
    ):AuthRepository {

    private val _register = MutableLiveData<FirebaseUser>()
    val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    val login: LiveData<FirebaseUser>
        get() = _login

    override fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(application,"회원가입 성공",Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(application,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }
    }

    override fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(application,"로그인 성공",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(application,"로그인 실패",Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                 }
             }
    }

    override fun logout(){
        auth.signOut()
    }

//    override suspend fun putID(key: String, value: String) {
//        val preferencesKey = stringPreferencesKey(key)
//        dataStore.edit { preferences ->
//            preferences[preferencesKey] = value
//        }
//    }

//    override suspend fun getID(key: String) {
//        return try {
//            val preferencesKey = stringPreferencesKey(key)
//            val preferences = dataStore.data.first()
//            preferences[preferencesKey]
//
//        } catch (e: Exception){
//            e.printStackTrace()
//            null
//        }
//    }


}