package com.example.chatapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat

class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MessageFragmentArgs>()

    private lateinit var chatViewModel: ChatViewModel
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
        chatViewModel = (activity as HomeActivity).chatViewModel
        val user = args.user
        (activity as HomeActivity).supportActionBar?.title = user.name

        binding.sendBtn.setOnClickListener {
            chatViewModel.sendMessage(
                binding.messageEdit.text.toString(),
                user.uid,
                SimpleDateFormat("HH:mm:ss").format(time)
            )

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}