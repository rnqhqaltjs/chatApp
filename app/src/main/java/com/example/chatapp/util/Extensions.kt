package com.example.chatapp.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.chatapp.MyApplication
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

val time = System.currentTimeMillis().toString()
fun View.hide(activity: Activity){
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    visibility = View.GONE
}

fun View.show(activity: Activity) {
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    visibility = View.VISIBLE
}

fun Fragment.toast(message: String?) {
    (requireActivity().application as MyApplication).showToast(requireActivity().applicationContext, message ?: "")
}

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun getLastMessageTimeString(lastTimeStamp: Long): String {           //마지막 메시지가 전송된 시각 구하기
    try {
        val lastTimeString = SimpleDateFormat("yyyyMMddHHmmss").format(lastTimeStamp)

        val messageMonth = lastTimeString.substring(4, 6).toInt()                   //마지막 메시지 시각 월,일,시,분
        val messageDate = lastTimeString.substring(6, 8).toInt()
        val messageHour = lastTimeString.substring(8, 10).toInt()
        val messageMinute = lastTimeString.substring(10, 12).toInt()

        val currentTime = System.currentTimeMillis() //현재 시각
        val formattedCurrentTimeString = SimpleDateFormat("yyyyMMddHHmmss").format(currentTime) //현 시각 월,일,시,분
        val currentMonth = formattedCurrentTimeString.substring(4, 6).toInt()
        val currentDate = formattedCurrentTimeString.substring(6, 8).toInt()
        val currentHour = formattedCurrentTimeString.substring(8, 10).toInt()
        val currentMinute = formattedCurrentTimeString.substring(10, 12).toInt()

        val monthAgo = currentMonth - messageMonth                           //현 시각과 마지막 메시지 시각과의 차이. 월,일,시,분
        val dayAgo = currentDate - messageDate
        val hourAgo = currentHour - messageHour
        val minuteAgo = currentMinute - messageMinute

        if (monthAgo > 0)                                         //1개월 이상 차이 나는 경우
            return monthAgo.toString() + "개월 전"
        else {
            return if (dayAgo > 0) {                                  //1일 이상 차이 나는 경우
                if (dayAgo == 1)
                    "어제"
                else
                    dayAgo.toString() + "일 전"
            } else {
                if (hourAgo > 0)
                    hourAgo.toString() + "시간 전"     //1시간 이상 차이 나는 경우
                else {
                    if (minuteAgo > 0)                       //1분 이상 차이 나는 경우
                        minuteAgo.toString() + "분 전"
                    else
                        "방금"
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return ""
    }
}

object ImageUtils {
    fun getBitmapFromUrl(urlString: String): Bitmap? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertFileToByteArray(context: Context, uri: Uri?): ByteArray? {
        return if (uri != null) {
            val orientation = getOrientationFromExif(context, uri)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val imageBitmap = BitmapFactory.decodeStream(inputStream)
            val rotatedBitmap = rotateBitmap(imageBitmap, orientation)
            val byteArrayOutputStream = ByteArrayOutputStream()
            rotatedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } else {
            null
        }
    }

    private fun getOrientationFromExif(context: Context, uri: Uri): Int {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }
        return 0
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap? {
        if (degrees != 0) {
            val matrix = Matrix()
            matrix.postRotate(degrees.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }
}


