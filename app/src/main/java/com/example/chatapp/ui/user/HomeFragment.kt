package com.example.chatapp.ui.user

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.databinding.FragmentHomeBinding
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    lateinit var userlistadapter: UserListAdapter

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

        userViewModel.getFriendsData()
        observer()
    }

    private fun recyclerview(){
        userlistadapter = UserListAdapter(userViewModel)
        binding.userRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = userlistadapter
        }
        userlistadapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToFragmentProfile(it)
            findNavController().navigate(action)
        }
        userlistadapter.setOnItemLongClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("알림")
                .setMessage("정말로 친구를 삭제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    userViewModel.removeFriend(it.uid)
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
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}