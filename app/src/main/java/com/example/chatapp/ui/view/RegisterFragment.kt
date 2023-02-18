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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel
import com.example.chatapp.util.convertFileToByteArray
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()
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

        authViewModel.register.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            Toast.makeText(context,"회원가입 성공",Toast.LENGTH_SHORT).show()
        }

        binding.imageArea.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)

        }

        binding.joinBtn.setOnClickListener {
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

    private fun validation(): Boolean {

        val name = binding.nameArea.text.toString()
        val email = binding.emailArea.text.toString()
        val password1 = binding.passwordArea1.text.toString()
        val password2 = binding.passwordArea2.text.toString()

        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (email.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (email.length<10||!email.contains("@")) {
            isValid = false
            Toast.makeText(requireContext(),"올바른 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (password1.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (password2.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (password1 != password2) {
            isValid = false
            Toast.makeText(requireContext(), "비밀번호가 서로 달라요.", Toast.LENGTH_SHORT).show()
        }
        if (password1.length < 6) {
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (!imageCheck) {
            isValid = false
            Toast.makeText(requireContext(), "앱에서 사용할 사진을 등록해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (email.length<10) {
            isValid = false
            Toast.makeText(requireContext(),"올바른 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}