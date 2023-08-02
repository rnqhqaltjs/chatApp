package com.example.chatapp.ui.detailphoto

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentDetailPhotoBinding
import dagger.hilt.android.AndroidEntryPoint

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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}