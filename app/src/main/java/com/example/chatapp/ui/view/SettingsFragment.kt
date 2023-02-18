package com.example.chatapp.ui.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.example.chatapp.databinding.FragmentSettingsBinding
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.example.chatapp.util.convertFileToByteArray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by activityViewModels<ChatViewModel>()
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.getProfileData({
            binding.profileImage.load(it)},
            {
            binding.profileName.setText(it)
        })

        binding.profileImage.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        binding.saveButton.setOnClickListener {
            if(binding.profileName.text.isNotEmpty() && imageUri!=null){
                chatViewModel.profileNameChange(binding.profileName.text.toString())
                chatViewModel.profileImageChange(convertFileToByteArray(requireContext(),imageUri!!))
                imageUri = null
            } else if (binding.profileName.text.isEmpty()){
                Toast.makeText(requireContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (imageUri == null) {
                chatViewModel.profileNameChange(binding.profileName.text.toString())
            }
        }

        binding.logout.setOnClickListener {
            chatViewModel.logout()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    //이미지 변경
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data //이미지 경로 원본
                binding.profileImage.setImageURI(imageUri) //이미지 뷰를 바꿈
                Log.d("image", "success")
            }
            else{
                Log.d("image", "failure")
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}