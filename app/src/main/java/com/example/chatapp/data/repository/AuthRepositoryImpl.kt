package com.example.chatapp.data.repository

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val application: Application,
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference
    ):AuthRepository {

    override suspend fun signup(
        name: String,
        email: String,
        image: ByteArray,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    storage.child("userImages").child("${auth.currentUser?.uid!!}/photo").putBytes(image)
                        .addOnSuccessListener {
                        var profileimage: Uri?

                        storage.child("userImages").child("${auth.currentUser?.uid!!}/photo").downloadUrl
                            .addOnSuccessListener {
                                profileimage = it

                                database.child("user")
                                    .child(auth.currentUser?.uid!!)
                                    .setValue(User(name,email,profileimage.toString(),auth.currentUser?.uid!!))
                            }
                    }
                    result.invoke(UiState.Success("로그인 성공"))
                } else {
                        try {
                            throw it.exception ?: java.lang.Exception("유효하지 않은 인증")
                        } catch (e: FirebaseAuthWeakPasswordException) {
                            result.invoke(UiState.Failure("비밀번호를 6자리 이상으로 입력해주세요"))
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            result.invoke(UiState.Failure("올바른 이메일을 입력해주세요"))
                        } catch (e: FirebaseAuthUserCollisionException) {
                            result.invoke(UiState.Failure("이미 사용중인 이메일입니다"))
                        } catch (e: Exception) {
                            result.invoke(UiState.Failure(e.message))
                        }
                }
            }
    }

    override suspend fun login(
        email: String,
        password: String,
        result: (UiState<String>)->Unit
    ){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("로그인 성공"))
                }
             }.addOnFailureListener {
                 result.invoke(UiState.Failure("인증 실패, 이메일과 패스워드를 확인하세요"))
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