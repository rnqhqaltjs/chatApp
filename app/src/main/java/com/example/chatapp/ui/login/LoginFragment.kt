package com.example.chatapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.ui.activity.HomeActivity
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

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
                loginViewModel.login(
                    email = binding.emailArea.text.toString(),
                    password = binding.passwordArea.text.toString()
                )
                loginViewModel.putID(binding.emailArea.text.toString())
            }
        }

        binding.sessionSaveBox.setOnCheckedChangeListener { _, isChecked ->
            loginViewModel.saveLoginBox(isChecked)
        }

        binding.signBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.findPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_findPasswordFragment)
        }

    }

    private fun observer(){
        loginViewModel.loginLiveData.observe(viewLifecycleOwner){ state ->
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
        val email = binding.emailArea.text.toString()
        val password = binding.passwordArea.text.toString()

        return when {
            email.isEmpty() -> {
                toast(getString(R.string.enter_email))
                false
            }
            !email.isValidEmail() -> {
                toast(getString(R.string.invalid_email))
                false
            }
            password.isEmpty() -> {
                toast(getString(R.string.enter_password))
                false
            }
            password.length < 6 -> {
                toast(getString(R.string.invalid_password))
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            binding.emailArea.setText(loginViewModel.getID())
            binding.sessionSaveBox.isChecked = loginViewModel.getLoginBox()
        }
    }
}