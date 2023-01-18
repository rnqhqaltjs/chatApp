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
    override val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    override val login: LiveData<FirebaseUser>
        get() = _login

    override suspend fun signup(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _register.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application,"중복된 이메일입니다",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override suspend fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _login.postValue(auth.currentUser)
                } else {
                    Toast.makeText(application,"아이디 또는 비밀번호를 제대로 입력해주세요",Toast.LENGTH_SHORT).show()
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