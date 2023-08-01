package com.example.chatapp.ui.detailphoto

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
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
            toast("다운로드 시작")
            downloadImage(args.message.photoImage)

        }
    }

    private fun downloadImage(imageUrl: String) {
        val request = DownloadManager.Request(Uri.parse(imageUrl))
            .setTitle(args.message.time)
            .setDescription("다운로드중....")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)

        val dm = context?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        dm.enqueue(request)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}