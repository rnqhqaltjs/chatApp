package com.example.chatapp.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.example.chatapp.databinding.FragmentEditProfileBinding
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by viewModels<EditProfileViewModel>()
    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        chatViewModel.getProfileData(
            { binding.profileImage.load(it) },
            { binding.profileName.setText(it) },
            { binding.profileEmail.text = it }
        )

        binding.profileImage.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)

        }

        binding.saveButton.setOnClickListener {
            if(binding.profileName.text.isNotEmpty()){
                chatViewModel.profileChange(
                    name = binding.profileName.text.toString(),
                    image = convertFileToByteArray(requireContext(),imageUri)
                )
            }
        }
    }

    //이미지 변경
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!! //이미지 경로 원본
                binding.profileImage.setImageURI(imageUri) //이미지 뷰를 바꿈
                Log.d("image", "success")
            }
            else{
                Log.d("image", "failure")
            }
        }

    private fun observer(){
        chatViewModel.profileobserve.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.profileProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.profileProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.profileProgress.hide(requireActivity())
                    toast(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}