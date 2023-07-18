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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<ProfileFragmentArgs>()

    private val time = System.currentTimeMillis()

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

        observer()

        chatViewModel.getRequest(args.user.uid)

        binding.friendRequestBtn.setOnClickListener {
            chatViewModel.friendRequest(args.user.uid, time.toString())
        }

        binding.requestCancelBtn.setOnClickListener {
            chatViewModel.requestCancel(args.user.uid)
        }
    }

    private fun observer() {
        chatViewModel.friendRequestObserver.observe(viewLifecycleOwner) { success ->
            when (success) {
                "pending" -> {
                    binding.friendRequestBtn.visibility = View.GONE
                    binding.requestCancelBtn.visibility = View.VISIBLE
                    binding.profileMessageBtn.visibility = View.GONE
                    binding.textView.text = "요청 취소"
                }
                "friends" -> {
                    binding.friendRequestBtn.visibility = View.GONE
                    binding.requestCancelBtn.visibility = View.GONE
                    binding.profileMessageBtn.visibility = View.VISIBLE
                    binding.textView.text = "1:1 채팅"
                }
                "nothing" -> {
                    binding.friendRequestBtn.visibility = View.VISIBLE
                    binding.requestCancelBtn.visibility = View.GONE
                    binding.profileMessageBtn.visibility = View.GONE
                    binding.textView.text = "친구 요청"
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}