package com.example.chatapp.data.repository

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ChatRepositoryImpl(
    private val application: Application,
    private val auth: FirebaseAuth,
    private val dbref: DatabaseReference,
    private val storage: FirebaseStorage
): ChatRepository {

    private val _currentuseradd = MutableLiveData<ArrayList<User>>()
    override val currentuseradd: LiveData<ArrayList<User>>
        get() = _currentuseradd

    private val _currentmessageadd = MutableLiveData<ArrayList<Message>>()
    override val currentmessageadd: LiveData<ArrayList<Message>>
        get() = _currentmessageadd

    private val _currentchatadd = MutableLiveData<ArrayList<Chat>>()
    override val currentchatadd: LiveData<ArrayList<Chat>>
        get() = _currentchatadd

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
                _currentuseradd.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(application,"유저 리스트를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                //실패 시 실행
            }
        })
    }

    override suspend fun sendMessage(message:String, receiverUid: String, time:String, image:String) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = receiverUid + senderUid
        val receiverRoom = senderUid + receiverUid

        val lastMsgObj: HashMap<String, Any> = HashMap()
        lastMsgObj["lastMsg"] = Message(message, senderUid!!, time, image).message
        dbref.child("chats").child(senderRoom).updateChildren(lastMsgObj)
        dbref.child("chats").child(receiverRoom).updateChildren(lastMsgObj)

        dbref.child("chats").child(senderRoom).child("messages").push()
            .setValue(Message(message, senderUid, time, image)).addOnSuccessListener {
                dbref.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(Message(message, senderUid, time, image))
            }


    }

    override suspend fun getMessageData(receiverUid: String) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = receiverUid + senderUid

        dbref.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList : ArrayList<Message> = arrayListOf()
                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        //유저 정보
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    _currentmessageadd.value = messageList
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application,"메시지를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
                    //실패 시 실행
                }
            })
    }

    override suspend fun getChatData() {
        val senderUid = auth.currentUser?.uid

        dbref.child("chats")
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

        dbref.child("user").child(uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue(User::class.java)

                    Toast.makeText(application,userProfile!!.name, Toast.LENGTH_SHORT).show()
                    image.invoke(userProfile.image)
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

        storage.reference.child("userImages/$uid/photo").delete().addOnSuccessListener {
            storage.reference.child("userImages/$uid/photo").putBytes(image).addOnSuccessListener {
                storage.reference.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                    val photoUri : Uri = it
                    dbref.child("users/$uid/image").setValue(photoUri.toString())
                    Toast.makeText(application, "프로필사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun logout(){
        auth.signOut()
    }
}