package com.example.chatapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.ui.adapter.UserListAdapter
import com.example.chatapp.ui.viewmodel.ChatViewModel
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var userlistadapter: UserListAdapter

    private val chatViewModel by activityViewModels<ChatViewModel>()

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

        recyclerview()

        chatViewModel.getUserData()
        chatViewModel.currentuseradd.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.homeProgress.show()
                }
                is UiState.Failure -> {
                    binding.homeProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.homeProgress.hide()
                    userlistadapter.submitList(state.data)
                }
            }
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