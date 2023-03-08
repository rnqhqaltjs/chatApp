package com.example.chatapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.ui.viewmodel.LoginViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.loginBtn.setOnClickListener {
            if(validation()){
                authViewModel.login(
                    email = binding.emailArea.text.toString(),
                    password = binding.passwordArea.text.toString()
                )
                authViewModel.putID(binding.emailArea.text.toString())
            }
        }

        binding.signBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    private fun observer(){
        authViewModel.login.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.loginBtn.text = ""
                    binding.loginProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.loginBtn.text = "로그인"
                    binding.loginProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.loginBtn.text = "로그인"
                    binding.loginProgress.hide(requireActivity())
                    toast(state.data)
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }

        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.emailArea.text.isNullOrEmpty()) {
            isValid = false
            toast("이메일을 입력해주세요")
        } else {
            if (!binding.emailArea.text.toString().isValidEmail()){
                isValid = false
                toast("올바른 이메일을 입력해주세요")
            }
        }
        if (binding.passwordArea.text.isEmpty()){
            isValid = false
            toast("비밀번호를 입력해주세요.")
        } else {
            if (binding.passwordArea.text.toString().length < 6){
                isValid = false
                toast("비밀번호를 6자리 이상으로 입력해주세요.")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        binding.emailArea.setText(authViewModel.getID())
    }
}