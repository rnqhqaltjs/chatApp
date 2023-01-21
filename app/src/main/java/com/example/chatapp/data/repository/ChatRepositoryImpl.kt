package com.example.chatapp.data.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.data.model.User
import com.example.chatapp.ui.adapter.UserListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl(
    private val application: Application,
    private val auth: FirebaseAuth,
    private val dbref: DatabaseReference
): ChatRepository {

    private val _current = MutableLiveData<ArrayList<User>>()
    override val current: LiveData<ArrayList<User>>
        get() = _current

    override suspend fun getUserData() {

        dbref.child("user").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val userList : ArrayList<User> = arrayListOf()

                for(postSnapshot in snapshot.children){
                    //유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                _current.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(application,"유저 리스트를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                //실패 시 실행
            }
        })
    }

    override fun logout(){
        auth.signOut()
    }
}