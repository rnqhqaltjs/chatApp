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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("로그인 성공"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("인증 실패, 이메일과 패스워드를 확인하세요"))
            }
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        image: ByteArray,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
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
                        result.invoke(UiState.Success("로그인 성공"))
                    }
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

    override suspend fun getLoginBox(): Flow<Boolean> {
        val preferenceKey = booleanPreferencesKey("saveIsLoginKeepAlive")
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

//    override suspend fun getID(key: String): String?{
//        return  try {
//            val preferenceKey = stringPreferencesKey(key)
//            val preference = dataStore.data.first()
//            preference[preferenceKey]
//        }catch (e:Exception){
//            e.printStackTrace()
//            null
//        }
//    }
}