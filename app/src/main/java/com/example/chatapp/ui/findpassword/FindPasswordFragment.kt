package com.example.chatapp.ui.findpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentFindPasswordBinding
import com.example.chatapp.ui.findpassword.FindPasswordViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordFragment : Fragment() {
    private var _binding: FragmentFindPasswordBinding? = null
    private val binding get() = _binding!!
    private val findPasswordViewModel: FindPasswordViewModel by viewModels()

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
        findPasswordViewModel.passwordResetLiveData.observe(viewLifecycleOwner) { state ->
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

    private fun validation(): Boolean {
        val email = binding.findPassText.text.toString().trim()

        return when {
            email.isEmpty() -> {
                toast(getString(R.string.enter_email))
                false
            }
            !email.isValidEmail() -> {
                toast(getString(R.string.invalid_email))
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}