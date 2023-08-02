package com.example.chatapp.ui.detailphoto

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.chatapp.R
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailPhotoViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    fun exitPhotoView(view: View) {
        view.findNavController().popBackStack()
    }

    val switch = ObservableBoolean(false)

    fun onDetailPhotoImageClick(currentSwitchState: Boolean) {
        switch.set(!currentSwitchState)
    }

    @SuppressLint("SimpleDateFormat")
    fun downloadImage(context: Context, message: Message) {
        Toast.makeText(context, "다운로드 시작", Toast.LENGTH_SHORT).show()
        val fileName =
            "/${context.getString(R.string.app_name)}/${SimpleDateFormat("yyyyMMddHHmmss").format(Date())}.jpg" // 이미지 파일 명

        val req = DownloadManager.Request(Uri.parse(message.photoImage))

        req.setTitle(fileName) // 제목
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // 알림 설정
            .setMimeType("image/*")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                fileName
            ) // 다운로드 완료 시 보여지는 이름

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(req)
    }
}