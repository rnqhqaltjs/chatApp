package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.viewmodel.ChatViewModel

class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MessageFragmentArgs>()

    private lateinit var chatViewModel: ChatViewModel

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
        chatViewModel = (activity as HomeActivity).chatViewModel

        val user = args.user

        (activity as HomeActivity).supportActionBar?.title = user.name
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}