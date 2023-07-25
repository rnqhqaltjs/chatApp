package com.example.chatapp.ui.register

import android.app.Activity.RESULT_OK
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
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<RegisterViewModel>()
    private lateinit var imageUri: Uri
    private var imageCheck = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.imageArea.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        binding.registerBtn.setOnClickListener {
            if(validation()){
                authViewModel.register(
                    binding.nameArea.text.toString(),
                    binding.emailArea.text.toString(),
                    convertFileToByteArray(requireContext(),imageUri)!!,
                    binding.passwordArea.text.toString()
                )
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
    //이미지 등록
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                imageUri = result.data?.data!! //이미지 경로 원본
                binding.imageArea.setImageURI(imageUri) //이미지 뷰를 바꿈
                imageCheck = true
                Log.d("image", "success")
            }
            else{
                Log.d("image", "failure")
            }
        }

    private fun observer() {
        authViewModel.registerLiveData.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.registerBtn.text = ""
                    binding.registerProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.registerBtn.text = "회원가입 하기"
                    binding.registerProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.registerBtn.text = "회원가입 하기"
                    binding.registerProgress.hide(requireActivity())
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }

    private fun validation(): Boolean {
        with(binding) {
            return when {
                nameArea.text.isNullOrEmpty() -> {
                    toast(getString(R.string.enter_name))
                    false
                }
                emailArea.text.isNullOrEmpty() -> {
                    toast(getString(R.string.enter_email))
                    false
                }
                !emailArea.text.toString().isValidEmail() -> {
                    toast(getString(R.string.invalid_email))
                    false
                }
                passwordArea.text.isNullOrEmpty() -> {
                    toast(getString(R.string.enter_password))
                    false
                }
                passwordArea.text.toString().length < 6 -> {
                    toast(getString(R.string.invalid_password))
                    false
                }
                confirmPasswordArea.text.isNullOrEmpty() -> {
                    toast(getString(R.string.enter_confirm_password))
                    false
                }
                passwordArea.text.toString() != confirmPasswordArea.text.toString() -> {
                    toast(getString(R.string.different_password))
                    false
                }
                !imageCheck -> {
                    toast(getString(R.string.enter_image))
                    false
                }
                else -> true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}