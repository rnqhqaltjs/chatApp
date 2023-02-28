package com.example.chatapp.ui.view

import android.app.Activity.RESULT_OK
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private var imageUri: Uri? = null
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
                    convertFileToByteArray(requireContext(),imageUri!!),
                    binding.passwordArea1.text.toString()
                )
            }
        }
    }
    //이미지 등록
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                imageUri = result.data?.data //이미지 경로 원본
                binding.imageArea.setImageURI(imageUri) //이미지 뷰를 바꿈
                imageCheck = true
                Log.d("image", "success")
            }
            else{
                Log.d("image", "failure")
            }
        }

    private fun observer() {
        authViewModel.register.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.registerBtn.text = ""
                    binding.registerProgress.show()
                }
                is UiState.Failure -> {
                    binding.registerBtn.text = "회원가입 하기"
                    binding.registerProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.registerBtn.text = "회원가입 하기"
                    binding.registerProgress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.nameArea.text.isNullOrEmpty()) {
            isValid = false
            toast("이름을 입력해주세요")
        }
        if (binding.emailArea.text.isNullOrEmpty()){
            isValid = false
            toast("이메일을 입력해주세요")
        }else{
            if (!binding.emailArea.text.toString().isValidEmail()){
                isValid = false
                toast("올바른 이메일 주소를 입력해주세요")
            }
        }
        if (binding.passwordArea1.text.isNullOrEmpty()){
            isValid = false
            toast("비밀번호를 입력해주세요")
        }else{
            if (binding.passwordArea1.text.toString().length < 6){
                isValid = false
                toast("비밀번호를 6자리 이상으로 입력해주세요")
            }
        }
        if (binding.passwordArea2.text.isNullOrEmpty()){
            isValid = false
            toast("비밀번호 확인을 입력해주세요")
        }
        if (binding.passwordArea1.text.toString() != binding.passwordArea2.text.toString()) {
            isValid = false
            toast("비밀번호가 서로 달라요")
        }
        if (!imageCheck) {
            isValid = false
            toast("앱에서 사용할 사진을 등록해주세요")
        }
        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}