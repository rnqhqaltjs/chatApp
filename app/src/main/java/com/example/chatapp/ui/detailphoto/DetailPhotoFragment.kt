package com.example.chatapp.ui.detailphoto

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.chatapp.databinding.FragmentDetailPhotoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPhotoFragment : Fragment() {
    private var _binding: FragmentDetailPhotoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailPhotoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = args.message
        binding.detailPhotoImage.load(message.photoImage)

        binding.exitPhotoView.setOnClickListener {
            findNavController().popBackStack()
        }

    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}