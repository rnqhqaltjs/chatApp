package com.example.chatapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.ui.viewmodel.AuthViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

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
                authViewModel.login(binding.emailArea.text.toString(), binding.passwordArea.text.toString())
            }
        }

        binding.signBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }

    private fun observer(){
        authViewModel.login.observe(viewLifecycleOwner){
            authViewModel.putID(binding.emailArea.text.toString())
            Toast.makeText(context,"로그인 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
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
        authViewModel.getID()
    }
}