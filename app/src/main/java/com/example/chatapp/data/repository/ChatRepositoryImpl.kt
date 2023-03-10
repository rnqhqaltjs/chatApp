package com.example.chatapp.data.repository

import android.net.Uri
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference

class ChatRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference
): ChatRepository {

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

    override suspend fun sendMessage(
        message:String,
        receiverUid: String,
        time:String,
        userReceiver: User
    ) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = senderUid + receiverUid
        val receiverRoom = receiverUid + senderUid

        database.child("user").child(senderUid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userSender = snapshot.getValue(User::class.java)

                    val theMessage = Message(message, senderUid, time, userSender!!.image,false)
                    val latestUserMessageForSender = Chat(theMessage, userReceiver)
                    val latestUserMessageForReceiver = Chat(theMessage, userSender)

                    database.child("chats").child(senderRoom).child("messages").push()
                        .setValue(theMessage).addOnSuccessListener {
                            database.child("chats").child(receiverRoom).child("messages").push()
                                .setValue(theMessage).addOnSuccessListener {

                                    database.child(("latestUsersAndMessages")).child(senderUid)
                                        .child(receiverUid)
                                        .setValue(latestUserMessageForSender)
                                        .addOnSuccessListener {
                                            database.child(("latestUsersAndMessages")).child(receiverUid)
                                                .child(senderUid)
                                                .setValue(latestUserMessageForReceiver)
                                        }
                                }
                        }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override suspend fun getMessageData(
        receiverUid: String,
        result: (UiState<List<Message>>) -> Unit
    ) {
        val senderUid = auth.currentUser?.uid
        val senderRoom = senderUid + receiverUid

        database.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList : ArrayList<Message> = arrayListOf()
                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    result.invoke(UiState.Success(messageList))
                }
                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("메세지를 불러오는데 실패했습니다"))
                }
            })
        seenMessage(receiverUid)
    }

    override fun seenMessage(receiverUid: String) {
        val senderUid = auth.currentUser?.uid
        val receiverRoom = receiverUid + senderUid
        val seenObj: HashMap<String, Any> = HashMap()
        seenObj["seen"] = true

        database.child("chats").child(receiverRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for(postSnapshot in snapshot.children){
                        postSnapshot.ref.updateChildren(seenObj)

                        database.child("latestUsersAndMessages")
                            .child(senderUid!!)
                            .child(receiverUid)
                            .child("message")
                            .updateChildren(seenObj)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override suspend fun getChatData(result: (UiState<List<Chat>>) -> Unit) {
        val senderUid = auth.currentUser?.uid

        database.child("latestUsersAndMessages").child(senderUid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList : ArrayList<Chat> = arrayListOf()
                    chatList.clear()

                    for(postSnapshot in snapshot.children){
                        val chat = postSnapshot.getValue(Chat::class.java)
                        chatList.add(chat!!)
                        chatList.sortBy { it.message.time }
                        chatList.reverse()
                    }
                    result.invoke(UiState.Success(chatList))
                }
                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("채팅리스트를 불러오는데 실패했습니다"))
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
                    //실패 시 실행
                }
            })
    }

    override suspend fun profileChange(name: String, image: ByteArray?, result: (UiState<String>)->Unit) {
        val uid = auth.currentUser?.uid

        if (image != null) {
            storage.child("userImages/$uid/photo").delete().addOnSuccessListener {
                storage.child("userImages/$uid/photo").putBytes(image).addOnSuccessListener {
                    storage.child("userImages/$uid/photo").downloadUrl.addOnSuccessListener {
                        val photoUri : Uri = it
                        database.child("user/$uid/image").setValue(photoUri.toString())
                        database.child("user/$uid/name").setValue(name)
                    }
                }
                result.invoke(UiState.Success("프로필 변경 완료"))
            }.addOnFailureListener {
                result.invoke(UiState.Failure("프로필 변경 과정 중 오류 발생"))
            }
        } else {
            database.child("user/$uid/name").setValue(name)
            result.invoke(UiState.Success("프로필 변경 완료"))
        }
    }

    override fun logout(){
        auth.signOut()
    }
}