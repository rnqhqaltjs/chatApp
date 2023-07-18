package com.example.chatapp.ui.findfriend

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentFindFriendBinding
import com.example.chatapp.ui.user.HomeFragmentDirections
import com.example.chatapp.ui.user.UserListAdapter
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindFriendFragment : Fragment() {
    private var _binding: FragmentFindFriendBinding? = null
    private val binding get() = _binding!!

    lateinit var userSearchAdapter: UserSearchAdapter

    private val chatViewModel by viewModels<FindFriendViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFindFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatViewModel.getUserData()

        recyclerview()

        observer()
    }

    private fun recyclerview(){
        userSearchAdapter = UserSearchAdapter()
        binding.userSearchRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(requireContext(),
                    DividerItemDecoration.VERTICAL)
            )
            adapter = userSearchAdapter
        }
    }

//    private fun searchUsers() {
//        var startTime = System.currentTimeMillis()
//        var endTime: Long
//
//        binding.etSearch.text =
//            Editable.Factory.getInstance().newEditable(bookSearchViewModel.query)
//
//        binding.etSearch.addTextChangedListener { text: Editable? ->
//            endTime = System.currentTimeMillis()
//            if (endTime - startTime >= 100L) {
//                text?.let {
//                    val query = it.toString().trim()
//                    if (query.isNotEmpty()) {
//                        chatViewModel.userSearch(query)
//                        bookSearchViewModel.query = query
//                    }
//                }
//            }
//            startTime = endTime
//        }
//    }

    private fun observer(){
        chatViewModel.userSearch.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.findFriendProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.findFriendProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.findFriendProgress.hide(requireActivity())
                    userSearchAdapter.submitList(state.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}