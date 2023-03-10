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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.ui.viewmodel.RegisterViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<RegisterViewModel>()
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
                    convertFileToByteArray(requireContext(),imageUri)!!,
                    binding.passwordArea1.text.toString()
                )
            }
        }
    }
    //????????? ??????
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                imageUri = result.data?.data //????????? ?????? ??????
                binding.imageArea.setImageURI(imageUri) //????????? ?????? ??????
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
                    binding.registerProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.registerBtn.text = "???????????? ??????"
                    binding.registerProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.registerBtn.text = "???????????? ??????"
                    binding.registerProgress.hide(requireActivity())
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
            toast("????????? ??????????????????")
        }
        if (binding.emailArea.text.isNullOrEmpty()){
            isValid = false
            toast("???????????? ??????????????????")
        }else{
            if (!binding.emailArea.text.toString().isValidEmail()){
                isValid = false
                toast("????????? ????????? ????????? ??????????????????")
            }
        }
        if (binding.passwordArea1.text.isNullOrEmpty()){
            isValid = false
            toast("??????????????? ??????????????????")
        }else{
            if (binding.passwordArea1.text.toString().length < 6){
                isValid = false
                toast("??????????????? 6?????? ???????????? ??????????????????")
            }
        }
        if (binding.passwordArea2.text.isNullOrEmpty()){
            isValid = false
            toast("???????????? ????????? ??????????????????")
        }
        if (binding.passwordArea1.text.toString() != binding.passwordArea2.text.toString()) {
            isValid = false
            toast("??????????????? ?????? ?????????")
        }
        if (!imageCheck) {
            isValid = false
            toast("????????? ????????? ????????? ??????????????????")
        }
        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}