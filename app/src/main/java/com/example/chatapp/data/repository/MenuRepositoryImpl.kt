package com.example.chatapp.data.repository

import android.net.Uri
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference

class MenuRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference
): MenuRepository {

    override fun logout(){
        auth.signOut()
    }

    override suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit), result: (UiState<String>) -> Unit) {
        val uid = auth.currentUser?.uid

        try {
            database.child("user").child(uid!!)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)

                        image.invoke(userProfile!!.image)
                        name.invoke(userProfile.name)
                        email.invoke(userProfile.email)
                        result.invoke(UiState.Success("프로필 불러오기 성공"))
                    }
                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure("프로필 불러오기 실패"))
                    }
                })
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }

    }

    override suspend fun profileChange(name: String, image: ByteArray?, result: (UiState<String>)->Unit) {
        val uid = auth.currentUser?.uid

        try {
            if (image != null) {
                storage.child("userImages/$uid/photo").delete().addOnSuccessListener {
                    storage.child("userImages/$uid/photo").putBytes(image).addOnSuccessListener {
                        storage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                            val photoUri : Uri = it
                            database.child("user/$uid/image").setValue(photoUri.toString())
                            database.child("user/$uid/name").setValue(name).addOnSuccessListener {
                                result.invoke(UiState.Success("프로필 변경 완료"))
                            }
                        }
                    }
                }.addOnFailureListener {
                    result.invoke(UiState.Failure("프로필 변경 과정 중 오류 발생"))
                }
            } else {
                database.child("user/$uid/name").setValue(name).addOnSuccessListener {
                    result.invoke(UiState.Success("프로필 변경 완료"))
                }
            }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun getUserSearchData(query: String, result: (UiState<List<User>>) -> Unit) {
        try {
            database.child("user").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList: ArrayList<User> = arrayListOf()
                    val uid = auth.currentUser?.uid

                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)

                        if (currentUser?.uid != uid) {
                            if(currentUser?.name == query){
                                userList.add(currentUser)
                            }
                        }
                    }
                    result.invoke(UiState.Success(userList))
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("유저 리스트를 불러오는데 실패했습니다"))
                }
            })
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

}