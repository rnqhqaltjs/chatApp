package com.example.chatapp.data.repository

import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import com.example.chatapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthRepositoryImpl(
    private val application: Application,
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth,
    private val dbref: DatabaseReference,
    private val storage: FirebaseStorage
    ):AuthRepository {

    companion object{
        val EMAIL_KEY = stringPreferencesKey("EMAIL")
    }

    private val _register = MutableLiveData<FirebaseUser>()
    override val register: LiveData<FirebaseUser>
        get() = _register

    private val _login = MutableLiveData<FirebaseUser>()
    override val login: LiveData<FirebaseUser>
        get() = _login

    override suspend fun signup(name: String, email: String, image: Uri, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    storage.reference.child("userImages").child("${auth.currentUser?.uid!!}/photo").putFile(image).addOnSuccessListener {
                        var profileimage: Uri?
                        storage.reference.child("userImages").child("${auth.currentUser?.uid!!}/photo").downloadUrl
                            .addOnSuccessListener {
                                profileimage = it
                                _register.postValue(auth.currentUser)
                                dbref.child("user")
                                    .child(auth.currentUser?.uid!!)
                                    .setValue(User(name,email,profileimage.toString(),auth.currentUser?.uid!!))
                            }
                    }


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

    override suspend fun putID(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        dataStore.edit{
            it[preferenceKey] = value
        }
    }

    override suspend fun getID(key: String): String?{
        return  try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = dataStore.data.first()
            preference[preferenceKey]
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

}