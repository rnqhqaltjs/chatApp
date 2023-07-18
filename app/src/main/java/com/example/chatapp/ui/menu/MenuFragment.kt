package com.example.chatapp.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.chatapp.databinding.FragmentMenuBinding
import com.example.chatapp.ui.activity.MainActivity
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by viewModels<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        chatViewModel.getProfileData(
            { binding.menuImage.load(it) },
            { binding.menuName.text = it },
            { binding.menuEmail.text = it }
        )

        binding.editProfileBtn.setOnClickListener {
            val action = MenuFragmentDirections.actionFragmentMenuToFragmentEditProfile()
            findNavController().navigate(action)
        }

        binding.findFriendBtn.setOnClickListener {
            val action = MenuFragmentDirections.actionFragmentMenuToFragmentFindFriend()
            findNavController().navigate(action)
        }

        binding.logoutBtn.setOnClickListener {
            chatViewModel.logout()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun observer() {
        chatViewModel.profileobserve.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.menuProgress.show(requireActivity())
                }

                is UiState.Failure -> {
                    binding.menuProgress.hide(requireActivity())
                    toast(state.error)
                }

                is UiState.Success -> {
                    binding.menuProgress.hide(requireActivity())
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}