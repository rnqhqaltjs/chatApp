package com.example.chatapp.ui.user

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentUserBinding
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    lateinit var userlistadapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview()

        userViewModel.getFriendsData()
        observer()

        binding.findFriendTitle.setOnClickListener {
            val action =  UserFragmentDirections.actionFragmentUserToFragmentFindFriend()
            findNavController().navigate(action)
        }
    }

    private fun recyclerview(){
        userlistadapter = UserListAdapter(userViewModel)
        binding.userRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userlistadapter
        }
        userlistadapter.setOnItemClickListener {
            val action = UserFragmentDirections.actionFragmentUserToFragmentProfile(it)
            findNavController().navigate(action)
        }
        userlistadapter.setOnItemLongClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("알림")
                .setMessage("정말로 친구를 삭제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    userViewModel.removeFriend(it.uid)
                    userViewModel.getFriendsData()
                    toast("친구 삭제 완료")
                }
                .setNegativeButton("아니오") { _, _ ->
                }
            builder.show()
        }
    }

    private fun observer(){
        userViewModel.userDataList.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.homeProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.homeProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.homeProgress.hide(requireActivity())
                    userlistadapter.submitList(state.data)

                    if(state.data.isEmpty()) {
                        binding.findFriendTitle.visibility = View.VISIBLE
                    } else {
                        binding.findFriendTitle.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}