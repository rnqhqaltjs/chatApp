package com.example.chatapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentFindPasswordBinding
import com.example.chatapp.ui.viewmodel.FindPasswordViewModel
import com.example.chatapp.ui.viewmodel.LoginViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordFragment : Fragment() {
    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!

    private val findPasswordViewModel by viewModels<FindPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFindPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.findPassBtn.setOnClickListener {
            if(validation()) {
                findPasswordViewModel.findPassword(binding.findPassText.text.toString())
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_findPasswordFragment_to_loginFragment)
        }
    }

    private fun observer(){
        findPasswordViewModel.findpassobserve.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.findPassBtn.text = ""
                    binding.findPassProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.findPassBtn.text = "전송됨"
                    binding.findPassProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.findPassBtn.text = "전송됨"
                    binding.findPassProgress.hide(requireActivity())
                    toast(state.data)
                }
            }
        }

    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.findPassText.text.isNullOrEmpty()){
            isValid = false
            toast("이메일을 입력해주세요")
        }else{
            if (!binding.findPassText.text.toString().isValidEmail()){
                isValid = false
                toast("올바른 이메일 주소를 입력해주세요")
            }
        }

        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}