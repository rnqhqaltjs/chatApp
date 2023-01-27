package com.example.chatapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.ui.adapter.UserListAdapter
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var userlistadapter: UserListAdapter

    private lateinit var chatViewModel: ChatViewModel

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

        recyclerview()

        chatViewModel.getUserData()
        chatViewModel.currentuseradd.observe(viewLifecycleOwner){
            userlistadapter.submitList(it)
        }

    }

    private fun recyclerview(){
        userlistadapter = UserListAdapter()
        binding.userRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
            adapter = userlistadapter
        }
        userlistadapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentMessage(it)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}