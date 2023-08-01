package com.example.chatapp.ui.detailphoto

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentDetailPhotoBinding
import com.example.chatapp.ui.profile.ProfileViewModel
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class DetailPhotoFragment : Fragment() {
    private var _binding: FragmentDetailPhotoBinding? = null
    private val binding get() = _binding!!
    private val detailPhotoViewModel: DetailPhotoViewModel by viewModels()
    private val args by navArgs<DetailPhotoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_photo, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = detailPhotoViewModel
        binding.message = args.message

        binding.exitPhotoView.bringToFront()
        binding.downloadPhotoView.bringToFront()


        binding.downloadPhotoView.setOnClickListener {
            downloadImage("https://firebasestorage.googleapis.com/v0/b/chatapp-19dab.appspot.com/o/chatImages%2FitOaTszVbRhikiVBoNQWV47tF583HHJ8QyPVlGXrEsKmmr6VXadTlnJ2%2F1690776304728?alt=media&token=ea540030-5285-4ee1-8c3d-dd29829ffc82")
        }
    }

    private fun downloadImage(imageUrl: String): String? {
        var filePath: String? = null
        var bitmap: Bitmap? = null

        try {
            val loader = ImageLoader(requireContext())
            val req = ImageRequest.Builder(requireContext())
                .data(imageUrl) // demo link
                .target { result ->
                    val bitmap2 = (result as BitmapDrawable).bitmap
                    bitmap = bitmap2
                }
                .build()

            val disposable = loader.enqueue(req)

            // 외부 저장소에 디렉토리 생성
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val directory = File(root, "YourDirectory") // 여기서 "YourDirectory"는 저장될 디렉토리 이름으로 원하는 이름으로 변경 가능합니다.
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // 파일 이름 설정
            val fileName = "YourImage.jpg" // 저장될 파일 이름으로 원하는 이름으로 변경 가능합니다.
            val file = File(directory, fileName)

            // 이미지 파일 저장
            val out: OutputStream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()

            // 파일 경로 반환
            filePath = file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return filePath
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}