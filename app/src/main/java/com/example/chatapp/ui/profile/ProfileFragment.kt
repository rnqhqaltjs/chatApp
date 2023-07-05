package com.example.chatapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ProfileFragmentArgs>()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = args.user

        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
        binding.profileImage.load(user.image)

        binding.profileMessageBtn.setOnClickListener {
            val action = ProfileFragmentDirections.actionFragmentProfileToFragmentMessage(user)
            findNavController().navigate(action)

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}