package com.example.chatapp.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.databinding.FragmentNotificationBinding
import com.example.chatapp.ui.user.HomeFragmentDirections
import com.example.chatapp.ui.user.HomeViewModel
import com.example.chatapp.ui.user.UserListAdapter
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    lateinit var friendRequestAdapter: FriendRequestAdapter

    private val chatViewModel by viewModels<NotificationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview()

        chatViewModel.getRequestData()
        observer()

    }

    private fun recyclerview(){
        friendRequestAdapter = FriendRequestAdapter(chatViewModel)
        binding.requestRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = friendRequestAdapter
        }
    }

    private fun observer(){
        chatViewModel.requestobserve.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.requestProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.requestProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.requestProgress.hide(requireActivity())
                    friendRequestAdapter.submitList(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}