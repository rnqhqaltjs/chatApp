package com.example.chatapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.adapter.MessageListAdapter
import com.example.chatapp.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MessageFragmentArgs>()
    lateinit var messageListAdapter: MessageListAdapter

    private val chatViewModel by viewModels<ChatViewModel>()
    private val time = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = args.user
        (activity as HomeActivity).supportActionBar?.title = user.name

        recyclerview()

        chatViewModel.getMessageData(user.uid)
        chatViewModel.currentmessageadd.observe(viewLifecycleOwner){
            messageListAdapter.submitList(it)
        }

        binding.sendBtn.setOnClickListener {
            chatViewModel.sendMessage(
                binding.messageEdit.text.toString(),
                user.uid,
                time.toString(),
                user.image
            )
            //입력값 초기화
            binding.messageEdit.setText("")

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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}