package com.example.chatapp.data.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.ui.view.HomeFragment
import com.example.chatapp.util.LoadingDialog
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ChatRepositoryImpl(
    private val application: Application,
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference
): ChatRepository {

    private val _currentchatadd = MutableLiveData<ArrayList<Chat>>()
    override val currentchatadd: LiveData<ArrayList<Chat>>
        get() = _currentchatadd

    override suspend fun getUserData(result: (UiState<List<User>>) -> Unit) {
        database.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList : ArrayList<User> = arrayListOf()

                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                    result.invoke(UiState.Success(userList))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                result.invoke(UiState.Failure("유저 리스트를 불러오는데 실패했습니다"))
            }
        })
    }

    override suspend fun sendMessage(message:String, receiverUid: String, time:String, image:String) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = receiverUid + senderUid
        val receiverRoom = senderUid + receiverUid

        database.child("user").child(senderUid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue(User::class.java)

                    database.child("chats").child(senderRoom).child("messages").push()
                        .setValue(Message(message, senderUid, time, userProfile!!.image)).addOnSuccessListener {
                            database.child("chats").child(receiverRoom).child("messages").push()
                                .setValue(Message(message, senderUid, time, userProfile.image))
                        }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application,"채팅리스트를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                    //실패 시 실행
                }
            })

//        val lastMsgObj: HashMap<String, Any> = HashMap()
//        lastMsgObj["lastMsg"] = Message(message, senderUid!!, time, image).message
//        dbref.child("chats").child(senderRoom).updateChildren(lastMsgObj)
//        dbref.child("chats").child(receiverRoom).updateChildren(lastMsgObj)

    }

    override suspend fun getMessageData(
        receiverUid: String,
        result: (UiState<List<Message>>) -> Unit
    ) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = receiverUid + senderUid

        database.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList : ArrayList<Message> = arrayListOf()
                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        //유저 정보
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    result.invoke(UiState.Success(messageList))
                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("메세지를 불러오는데 실패했습니다"))
                }
            })
    }

    override suspend fun getChatData() {
        val senderUid = auth.currentUser?.uid

        database.child("chats")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList : ArrayList<Chat> = arrayListOf()

                    for(postSnapshot in snapshot.children){

                    }
                    _currentchatadd.value = chatList
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application,"채팅리스트를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                    //실패 시 실행
                }
            })
    }

    override suspend fun getProfileData(image: ((String)->Unit), name: ((String)->Unit)) {
        val uid = auth.currentUser?.uid

        database.child("user").child(uid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue(User::class.java)

                    image.invoke(userProfile!!.image)
                    name.invoke(userProfile.name)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application,"채팅리스트를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                    //실패 시 실행
                }
            })
    }

    override suspend fun profileImageChange(image: ByteArray) {
        val uid = auth.currentUser?.uid

        storage.child("userImages/$uid/photo").delete().addOnSuccessListener {
            storage.child("userImages/$uid/photo").putBytes(image).addOnSuccessListener {
                storage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                    val photoUri : Uri = it
                    database.child("user/$uid/image").setValue(photoUri.toString())
                    Toast.makeText(application, "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override suspend fun profileNameChange(name: String) {
        val uid = auth.currentUser?.uid
        database.child("user/$uid/name").setValue(name)
        Toast.makeText(application, "프로필이름이 변경되었습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun logout(){
        auth.signOut()
    }
}