package com.example.chatapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.chatapp.R
import com.example.chatapp.data.model.User
import com.example.chatapp.ui.activity.HomeActivity
import com.example.chatapp.ui.activity.MainActivity
import com.example.chatapp.ui.chat.ChatFragmentDirections
import com.example.chatapp.util.ImageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MyFirebaseMessagingService : FirebaseMessagingService() {
    /** 푸시 알림으로 보낼 수 있는 메세지는 2가지
     * 1. Notification: 앱이 실행중(포그라운드)일 떄만 푸시 알림이 옴
     * 2. Data: 실행중이거나 백그라운드(앱이 실행중이지 않을때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */

    private val TAG = "FirebaseService"

    /** Token 생성 메서드(FirebaseInstanceIdService 사라짐) */
    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")
    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        //Log.d(TAG, "notify title: ${remoteMessage.notification?.title}")
        // 다른 기기에서 서버로 보냈을 때
        if(remoteMessage.data.isNotEmpty()){
            val gson = Gson()
            val user = gson.fromJson(remoteMessage.data["user"], User::class.java)
            val senderUid = remoteMessage.data["senderUid"]!!
            val message = remoteMessage.data["message"]!!
            val type = remoteMessage.data["type"]!!.toInt()

            when (type) {
                0 -> sendMessageNotification(user, senderUid, message, type)
                1 -> friendRequestNotification(user, senderUid, message, type)
                else -> throw IllegalArgumentException("Invalid type: $type") // 0 또는 1 이외의 값이 들어오는 경우 예외 처리
            }
        }
    }

    /** 알림 생성 메서드 */
    private fun friendRequestNotification(user: User, senderUid: String, message: String, type: Int){
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
//        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        val pendingIntent: PendingIntent = if (currentUserUid == null || currentUserUid != senderUid) {
            NavDeepLinkBuilder(this)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.auth_navigation)
                .setDestination(R.id.loginFragment)
                .createPendingIntent()

        } else {
            NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_navigation)
                .setDestination(R.id.fragment_user)
                .createPendingIntent()
        }


        // 알림 채널 이름
        val channelId = "my_channel"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(ImageUtils.getBitmapFromUrl(user.image)) // 아이콘 설정
            .setContentTitle(user.name) // 제목
            .setContentText(message) // 메시지 내용
            .setAutoCancel(true) // 알람클릭시 삭제여부
            .setSound(soundUri)  // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(type, notificationBuilder.build())
    }

    private fun sendMessageNotification(user: User, senderUid: String, message: String, type: Int){
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
//        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        val pendingIntent: PendingIntent = if (currentUserUid == null || currentUserUid != senderUid) {
            NavDeepLinkBuilder(this)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.auth_navigation)
                .setDestination(R.id.loginFragment)
                .createPendingIntent()

        } else {
            NavDeepLinkBuilder(this)
                .setComponentName(HomeActivity::class.java)
                .setGraph(R.navigation.home_navigation)
                .setDestination(R.id.fragment_message)
                .setArguments(
                    ChatFragmentDirections.actionFragmentChatToFragmentMessage(
                        User(user.name, user.email, user.image, user.uid, user.token)
                    )
                        .arguments
                )
                .createPendingIntent()
        }

        // 알림 채널 이름
        val channelId = "my_channel"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(ImageUtils.getBitmapFromUrl(user.image)) // 아이콘 설정
            .setContentTitle(user.name) // 제목
            .setContentText(message) // 메시지 내용
            .setAutoCancel(true) // 알람클릭시 삭제여부
            .setSound(soundUri)  // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(type, notificationBuilder.build())
    }

}