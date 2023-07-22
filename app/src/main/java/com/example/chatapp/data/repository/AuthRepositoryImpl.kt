package com.example.chatapp.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.chatapp.data.model.User
import com.example.chatapp.util.Constants.CHECK_BOX
import com.example.chatapp.util.Constants.USER_NAME
import com.example.chatapp.util.UiState
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference,
):AuthRepository {

    override suspend fun loginUser(
        email: String,
        password: String,
        result: (UiState<String>)->Unit
    ){
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.invoke(UiState.Success("로그인 성공"))
                    } else {
                        result.invoke(UiState.Failure("인증 실패, 이메일과 패스워드를 확인하세요"))
                    }
                }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        image: ByteArray,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        //FCM 불러오기
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            // 실패
                            if (!task.isSuccessful) {
                                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                                return@addOnCompleteListener
                            }
                            // 받아온 새로운 토큰
                            val token = task.result

                            storage.child("userImages").child("${auth.currentUser?.uid!!}/photo").putBytes(image)
                                .addOnSuccessListener {
                                    var profileimage: Uri?

                                    storage.child("userImages").child("${auth.currentUser?.uid!!}/photo").downloadUrl
                                        .addOnSuccessListener {
                                            profileimage = it

                                            database.child("user")
                                                .child(auth.currentUser?.uid!!)
                                                .setValue(User(name,email,profileimage.toString(),auth.currentUser?.uid!!,token))
                                        }
                                }
                            result.invoke(UiState.Success("회원가입 성공"))
                        }
                    } else {
                        result.invoke(UiState.Failure("회원가입 실패"))
                    }
                }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun findPassword(email: String, result: (UiState<String>) -> Unit) {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.invoke(UiState.Success("이메일 전송 성공"))

                    } else {
                        result.invoke(UiState.Failure("전송 실패, 이메일을 확인하세요"))
                    }
                }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun putID(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        dataStore.edit{ prefs ->
            prefs[preferenceKey] = value
        }
    }

    override suspend fun getID(key: String): Flow<String> {
        val preferenceKey = stringPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[preferenceKey] ?: USER_NAME
            }
    }

    override suspend fun saveLoginBox(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        dataStore.edit{ prefs ->
            prefs[preferenceKey] = value
        }
    }

    override suspend fun getLoginBox(key: String): Flow<Boolean> {
        val preferenceKey = booleanPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { prefs ->
                prefs[preferenceKey] ?: false
            }
    }
}