package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentRegisterBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

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

        binding.joinBtn.setOnClickListener {
            if(validation()){
                authViewModel.register(binding.emailArea.text.toString(), binding.passwordArea1.text.toString())
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        val email: String = binding.emailArea.text.toString()
        val password1: String = binding.passwordArea1.text.toString()
        val password2: String = binding.passwordArea2.text.toString()

        if (email.isEmpty()) {
            isValid = false
            Toast.makeText(requireContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (email.length<10){
            isValid = false
            Toast.makeText(requireContext(),"올바른 이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        if (password1.isEmpty()){
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (password2.isEmpty()){
            isValid = false
            Toast.makeText(requireContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if (password1 != password2) {
            isValid = false
            Toast.makeText(requireContext(), "비밀번호가 서로 달라요.", Toast.LENGTH_SHORT).show()
        }
        if (password1.length < 6){
            isValid = false
            Toast.makeText(requireContext(), "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}