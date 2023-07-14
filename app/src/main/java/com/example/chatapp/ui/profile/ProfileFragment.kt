package com.example.chatapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentProfileBinding
import com.example.chatapp.ui.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<ProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = args.user

        chatViewModel.friendRequestObserver.observe(viewLifecycleOwner) { success ->
            if(success == "pending") {
                binding.friendRequestBtn.visibility = View.GONE
                binding.requestCancelBtn.visibility = View.VISIBLE
                binding.friendRequestText.text = "요청 취소"
            } else {
                binding.friendRequestBtn.visibility = View.VISIBLE
                binding.requestCancelBtn.visibility = View.GONE
                binding.friendRequestText.text = "친구 요청"

            }
        }

        chatViewModel.getRequest(args.user.uid)

        binding.friendRequestBtn.setOnClickListener {
            chatViewModel.friendRequest(args.user.uid)
        }

        binding.requestCancelBtn.setOnClickListener {
            chatViewModel.requestCancel(args.user.uid)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}