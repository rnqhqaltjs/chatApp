package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.ui.adapter.ChatListAdapter
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    lateinit var chatlistadapter: ChatListAdapter

    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview()
        chatViewModel.getChatData()
        observer()
    }

    private fun recyclerview(){
        chatlistadapter = ChatListAdapter()
        binding.chatRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                    DividerItemDecoration.VERTICAL)
            )
            adapter = chatlistadapter
        }
        chatlistadapter.setOnItemClickListener {
            val action = ChatFragmentDirections.actionFragmentChatToFragmentMessage(it.user)
            findNavController().navigate(action)
        }
    }

    private fun observer(){
        chatViewModel.chatobserve.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.chatProgress.show()
                }
                is UiState.Failure -> {
                    binding.chatProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.chatProgress.hide()
                    chatlistadapter.submitList(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}