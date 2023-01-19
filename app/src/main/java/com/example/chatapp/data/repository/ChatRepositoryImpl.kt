package com.example.chatapp.data.repository

import com.example.chatapp.data.model.User
import com.example.chatapp.ui.adapter.UserListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ChatRepositoryImpl(
    private val auth: FirebaseAuth,
    private val dbref: DatabaseReference
): ChatRepository {
    override suspend fun getUserData(userList: ArrayList<User>, adapter: UserListAdapter){

        dbref.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    //유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //실패 시 실행
            }
        })
    }
}