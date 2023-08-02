package com.example.chatapp.data.repository

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import com.example.chatapp.data.api.RetrofitInstance
import com.example.chatapp.data.model.Chat
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.NotificationBody
import com.example.chatapp.data.model.Request
import com.example.chatapp.data.model.User
import com.example.chatapp.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class ChatRepositoryImpl(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    private val storage: StorageReference
): ChatRepository {

    private val senderUid: String?
        get() = auth.currentUser?.uid

    override suspend fun getFriendsData(result: (UiState<List<User>>) -> Unit) {
        try {
            database.child("user").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList: ArrayList<User> = arrayListOf()
                    val friendUids: ArrayList<String> = arrayListOf()

                    // 친구 uid 목록 가져오기
                    database.child("friends").child(senderUid!!).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(friendsSnapshot: DataSnapshot) {
                            friendUids.clear()

                            for (friendSnapshot in friendsSnapshot.children) {
                                friendUids.add(friendSnapshot.key!!)
                            }

                            // 유저 데이터 가져오기 및 친구인 유저만 추가
                            for (postSnapshot in snapshot.children) {
                                val currentUser = postSnapshot.getValue(User::class.java)

                                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                    // 실패
                                    if (!task.isSuccessful) {
                                        Log.d(
                                            ContentValues.TAG,
                                            "Fetching FCM registration token failed",
                                            task.exception
                                        )
                                        return@addOnCompleteListener
                                    }
                                    // 받아온 새로운 토큰
                                    val token = task.result

                                    if (currentUser?.token != token) {
                                        database.child("user/$senderUid/token").setValue(token)
                                    }
                                }

                                if (currentUser?.uid != null && currentUser.uid != senderUid && currentUser.uid in friendUids) {
                                    userList.add(currentUser)
                                }
                            }

                            result.invoke(UiState.Success(userList))
                        }
                        override fun onCancelled(friendsError: DatabaseError) {
                            result.invoke(UiState.Failure("친구 목록을 불러오는데 실패했습니다"))
                        }
                    })
                }
                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("친구 목록을 불러오는데 실패했습니다"))
                }
            })
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun sendMessage(
        message:String,
        receiverUid: String,
        time:String,
        userReceiver: User
    ) {
        val senderRoom = senderUid + receiverUid
        val receiverRoom = receiverUid + senderUid

        database.child("user").child(senderUid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userSender = snapshot.getValue(User::class.java)

                    val theMessage = Message(message, senderUid!!, time, userSender!!.image, "", false)
                    val latestUserMessageForSender = Chat(theMessage, userReceiver)
                    val latestUserMessageForReceiver = Chat(theMessage, userSender)

                    database.child("chats").child(senderRoom).child("messages").push()
                        .setValue(theMessage).addOnSuccessListener {
                            database.child("chats").child(receiverRoom).child("messages").push()
                                .setValue(theMessage).addOnSuccessListener {

                                    database.child(("latestUsersAndMessages")).child(senderUid!!)
                                        .child(receiverUid)
                                        .setValue(latestUserMessageForSender)
                                        .addOnSuccessListener {
                                            database.child(("latestUsersAndMessages")).child(receiverUid)
                                                .child(senderUid!!)
                                                .setValue(latestUserMessageForReceiver)
                                        }
                                }
                        }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override suspend fun sendImageMessage(
        message: String,
        image: ByteArray?,
        receiverUid: String,
        time:String,
        userReceiver: User,
        result: (UiState<String>) -> Unit
    ) {
        val senderRoom = senderUid + receiverUid
        val receiverRoom = receiverUid + senderUid

        try {
            database.child("user").child(senderUid!!)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userSender = snapshot.getValue(User::class.java)

                        storage.child("chatImages").child(senderRoom).child(time).putBytes(image!!)
                            .addOnSuccessListener {
                                var chatimage: Uri?

                                storage.child("chatImages").child(senderRoom).child(time).downloadUrl
                                    .addOnSuccessListener {
                                        chatimage = it

                                        val theMessage = Message(message, senderUid!!, time, userSender!!.image, chatimage.toString(), false)
                                        val latestUserMessageForSender = Chat(theMessage, userReceiver)
                                        val latestUserMessageForReceiver = Chat(theMessage, userSender)

                                        database.child("chats").child(senderRoom).child("messages").push()
                                            .setValue(theMessage).addOnSuccessListener {
                                                database.child("chats").child(receiverRoom).child("messages").push()
                                                    .setValue(theMessage).addOnSuccessListener {

                                                        database.child(("latestUsersAndMessages")).child(senderUid!!)
                                                            .child(receiverUid)
                                                            .setValue(latestUserMessageForSender)
                                                            .addOnSuccessListener {

                                                                database.child(("latestUsersAndMessages")).child(receiverUid)
                                                                    .child(senderUid!!)
                                                                    .setValue(latestUserMessageForReceiver)
                                                                    .addOnSuccessListener {
                                                                        result.invoke(UiState.Success("이미지 전송 성공"))
                                                                    }
                                                            }
                                                    }
                                            }
                                    }
                            }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        result.invoke(UiState.Failure("이미지 전송 실패"))
                    }
                })
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun getMessageData(
        receiverUid: String,
        result: (UiState<List<Message>>) -> Unit
    ) {
        val senderRoom = senderUid + receiverUid

        try {
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
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    private lateinit var messageSeenListener: ValueEventListener
    private lateinit var latestMessageSeenListener: ValueEventListener

    override fun seenMessage(receiverUid: String) {
        val receiverRoom = receiverUid + senderUid
        val seenObj: HashMap<String, Any> = HashMap()
        seenObj["seen"] = true

        messageSeenListener = database.child("chats").child(receiverRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(postSnapshot in snapshot.children){
                        postSnapshot.ref.updateChildren(seenObj)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

        latestMessageSeenListener = database.child("latestUsersAndMessages").child(senderUid!!).child(receiverUid)
            .child("message")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        snapshot.ref.updateChildren(seenObj)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun removeSeenMessage(receiverUid: String) {
        val receiverRoom = receiverUid + senderUid
        database.child("chats").child(receiverRoom).child("messages")
            .removeEventListener(messageSeenListener)

        database.child("latestUsersAndMessages").child(senderUid!!).child(receiverUid)
            .child("message")
            .removeEventListener(latestMessageSeenListener)
    }

    override suspend fun getChatData(result: (UiState<List<Chat>>) -> Unit) {
        try {
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
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun getNonSeenData(count: ((Int)->Unit)) {
        database.child("latestUsersAndMessages").child(senderUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var unreadMessageCount = 0

                    for (postSnapshot in snapshot.children) {
                        val chat = postSnapshot.getValue(Chat::class.java)
                        if(!chat!!.message.seen) {
                            unreadMessageCount++
                        }
                    }
                    count.invoke(unreadMessageCount)
                }

                override fun onCancelled(error: DatabaseError) {
                    // 실패 시 실행
                }
            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun sendMessageNotification(
        message:String,
        userReceiver: User,
        result: (UiState<String>) -> Unit
    ) {
        try {
            val userSender = suspendCancellableCoroutine { continuation ->
                database.child("user").child(senderUid!!)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            continuation.resume(user, null)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            continuation.resume(null, null)
                        }
                    })
            }

            val data = NotificationBody.NotificationData(userReceiver.uid, userSender!!.name, message, userSender.image, 0)
            val body = NotificationBody(userReceiver.token, data)

            val response = RetrofitInstance.api.sendNotification(body)

            if (response.isSuccessful) {
                result.invoke(UiState.Success("메시지 전송 성공"))
            } else {
                result.invoke(UiState.Failure("푸시 메시지 전송에 실패했습니다"))
            }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun checkFriendRequestStatus(receiverUid: String, result: (String) -> Unit) {
        database.child("requests").child(receiverUid).child(senderUid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    result.invoke("pending")
                } else {
                    database.child("friends").child(senderUid!!).child(receiverUid).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                result.invoke("friend")
                            } else {
                                result.invoke("nothing")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            result.invoke("nothing")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                result.invoke("nothing")
            }
        })
    }

    override suspend fun friendRequest(receiverUid: String, time: String, result: (String)->Unit) {
        val request: HashMap<String, Any> = HashMap()
        request["status"] = "pending"

        database.child("user").child(senderUid!!)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userSender = snapshot.getValue(User::class.java)

                    database.child("requests").child(receiverUid).child(senderUid!!).setValue(Request(userSender!!.name, senderUid!!, "pending", time, userSender.image))
                        .addOnSuccessListener {
                            result.invoke("pending")
                        }. addOnFailureListener {
                            result.invoke("nothing")
                        }

                }

                override fun onCancelled(error: DatabaseError) {
                    result.invoke("nothing")
                }
            })
    }

    override suspend fun requestCancel(receiverUid: String, result: (String)->Unit) {
        database.child("requests").child(receiverUid).child(senderUid!!).removeValue()
            .addOnSuccessListener {
                result.invoke("nothing")
            }. addOnFailureListener {
                result.invoke("pending")
            }
    }

    override suspend fun getRequestData(result: (UiState<List<Request>>) -> Unit) {
        try {
            database.child("requests").child(senderUid!!).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val requestList : ArrayList<Request> = arrayListOf()
                    requestList.clear()

                    for(postSnapshot in snapshot.children){
                        val requestUser = postSnapshot.getValue(Request::class.java)

                        requestList.add(requestUser!!)
                    }
                    result.invoke(UiState.Success(requestList))
                }
                override fun onCancelled(error: DatabaseError) {
                    result.invoke(UiState.Failure("친구 요청 리스트를 불러오는데 실패했습니다"))
                }
            })
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }

    override suspend fun declineRequest(receiverUid: String) {
        database.child("requests").child(senderUid!!).child(receiverUid).removeValue()
    }

    override suspend fun acceptRequest(receiverUid: String) {
        val request: HashMap<String, Any> = HashMap()
        request["status"] = "friend"

        database.child("requests").child(senderUid!!).child(receiverUid).removeValue().addOnSuccessListener {
            database.child("friends").child(receiverUid).child(senderUid!!).updateChildren(request)
            database.child("friends").child(senderUid!!).child(receiverUid).updateChildren(request)
        }
    }

    override suspend fun removeFriend(receiverUid: String) {
        database.child("friends").child(senderUid!!).child(receiverUid).removeValue()
        database.child("friends").child(receiverUid).child(senderUid!!).removeValue()
        database.child(("latestUsersAndMessages")).child(senderUid!!).child(receiverUid).removeValue()
        database.child(("latestUsersAndMessages")).child(receiverUid).child(senderUid!!).removeValue()
    }

    override suspend fun getRequestCount(count: (Int) -> Unit) {
        database.child("requests").child(senderUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var unreadMessageCount = 0

                    for (postSnapshot in snapshot.children) {
                        unreadMessageCount++
                    }
                    count.invoke(unreadMessageCount)
                }

                override fun onCancelled(error: DatabaseError) {
                    // 실패 시 실행
                }
            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun friendRequestNotification(
        message: String,
        userReceiver: User,
        result: (UiState<String>) -> Unit
    ) {
        try {
            val userSender = suspendCancellableCoroutine { continuation ->
                database.child("user").child(senderUid!!)
                    .addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            continuation.resume(user, null)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            continuation.resume(null, null)
                        }
                    })
            }

            val data = NotificationBody.NotificationData(userReceiver.uid, userSender!!.name, message, userSender.image, 1)
            val body = NotificationBody(userReceiver.token, data)

            val response = RetrofitInstance.api.sendNotification(body)

            if (response.isSuccessful) {
                result.invoke(UiState.Success("친구 요청 성공"))
            } else {
                result.invoke(UiState.Failure("친구 요청 전송에 실패했습니다"))
            }
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message))
        }
    }
}