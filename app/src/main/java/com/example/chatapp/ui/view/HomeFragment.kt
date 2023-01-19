package com.example.chatapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.ui.adapter.UserListAdapter
import com.example.chatapp.ui.viewmodel.ChatViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: UserListAdapter

    private lateinit var chatViewModel: ChatViewModel
    private lateinit var userList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel = (activity as HomeActivity).chatViewModel
        userList = ArrayList()
        adapter = UserListAdapter()

        binding.userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.userRecyclerview.adapter = adapter

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        chatViewModel.getUserData(userList, adapter)
    }
}