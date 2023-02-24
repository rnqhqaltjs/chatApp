package com.example.chatapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()

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
                    binding.loginBtn.text = "..."
                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
                    binding.loginBtn.text = "로그인"
                    binding.loginProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.loginBtn.text = "로그인"
                    binding.loginProgress.hide()
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

        val email: String = binding.emailArea.text.toString()
        val password: String = binding.passwordArea.text.toString()

        if (email.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (email.length<10){
            isValid = false
            Toast.makeText(requireContext(),"올바른 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (password.isEmpty()){
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (password.length < 6){
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show()
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