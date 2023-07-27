package com.example.chatapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.chatapp.R
import com.example.chatapp.ui.activity.HomeActivity
import com.example.chatapp.util.getBitmapFromUrl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
            val name = remoteMessage.data["name"]!!
            val message = remoteMessage.data["message"]!!
            val image = remoteMessage.data["image"]!!
            val type = remoteMessage.data["type"]!!.toInt()
            val icon = getBitmapFromUrl(image)

            when (type) {
                0 -> sendMessageNotification(name, message, icon!!, type)
                1 -> friendRequestNotification(name, message, icon!!, type)
                else -> throw IllegalArgumentException("Invalid type: $type") // 0 또는 1 이외의 값이 들어오는 경우 예외 처리
            }
        }
    }

    /** 알림 생성 메서드 */
    private fun friendRequestNotification(name: String, message: String, icon: Bitmap, type: Int){
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
//        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(HomeActivity::class.java)
            .setGraph(R.navigation.home_navigation)
            .setDestination(R.id.fragment_notification)
//            .setArguments(args)
            .createPendingIntent()

        // 알림 채널 이름
        val channelId = "my_channel"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(icon) // 아이콘 설정
            .setContentTitle(name) // 제목
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

    private fun sendMessageNotification(name: String, message: String, icon: Bitmap, type: Int){
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
//        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(HomeActivity::class.java)
            .setGraph(R.navigation.home_navigation)
            .setDestination(R.id.fragment_chat)
//            .setArguments(args)
            .createPendingIntent()

        // 알림 채널 이름
        val channelId = "my_channel"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(icon) // 아이콘 설정
            .setContentTitle(name) // 제목
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