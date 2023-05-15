package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.adapter.MessageListAdapter
import com.example.chatapp.ui.viewmodel.MessageViewModel
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MessageFragmentArgs>()
    lateinit var messageListAdapter: MessageListAdapter

    private val chatViewModel by viewModels<MessageViewModel>()
    private val time = System.currentTimeMillis()

    private lateinit var user: User // 클래스 변수로 user 선언

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = args.user
        (activity as HomeActivity).supportActionBar?.title = user.name

        recyclerview()

        chatViewModel.getMessageData(user.uid)
        observer()

        binding.sendBtn.setOnClickListener {
            if(binding.messageEdit.text.toString().isNotEmpty()){
                chatViewModel.sendMessage(
                    binding.messageEdit.text.toString(),
                    user.uid,
                    time.toString(),
                    user
                )
                chatViewModel.sendNotification(binding.messageEdit.text.toString(), user)
                //입력값 초기화
                binding.messageEdit.setText("")
            }
        }
    }

    private fun recyclerview(){
        messageListAdapter = MessageListAdapter()
        binding.messageRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = messageListAdapter
        }
    }

    private fun observer(){
        chatViewModel.messageobserve.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.messageProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.messageProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.messageProgress.hide(requireActivity())
                    messageListAdapter.submitList(state.data)

                    //메시지 마지막 아이템으로 이동
                    if (messageListAdapter.itemCount > 0) {
                        binding.messageRecyclerView.postDelayed({
                            binding.messageRecyclerView.smoothScrollToPosition(messageListAdapter.itemCount - 1)
                        }, 100)
                    }
                }
            }
        }

        chatViewModel.notifyobserve.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                }
            }
        }
    }

    override fun onPause() {
        chatViewModel.removeSeenMessage(user.uid)
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}