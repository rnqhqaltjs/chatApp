package com.example.chatapp.ui.message

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.activity.HomeActivity
import com.example.chatapp.util.ImageUtils
import com.example.chatapp.util.UiState
import com.example.chatapp.util.hide
import com.example.chatapp.util.show
import com.example.chatapp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<MessageFragmentArgs>()
    private val messageViewModel: MessageViewModel by viewModels()
    lateinit var messageListAdapter: MessageListAdapter
    private lateinit var imageUri: Uri
    private lateinit var user: User
    private var isPhotoSelectionOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = messageViewModel
        user = args.user
        (activity as HomeActivity).supportActionBar?.title = user.name

        recyclerview()

        messageViewModel.getMessageData(user.uid)
        observer()

        binding.sendImageBtn.setOnClickListener {
            isPhotoSelectionOpen = true
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        binding.sendBtn.setOnClickListener {
            val message = binding.messageEdit.text.toString()

            if(message.isNotEmpty()){
                messageViewModel.sendMessage(
                    message,
                    user.uid,
                    user
                )
                messageViewModel.sendMessageNotification(message, user)
                //입력값 초기화
                binding.messageEdit.setText("")
            }
        }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if(result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!! //이미지 경로 원본

                messageViewModel.sendImageMessage(
                    "이미지를 전송하였습니다.",
                    ImageUtils.convertFileToByteArray(requireContext(),imageUri),
                    user.uid,
                    user
                )
                messageViewModel.sendMessageNotification("이미지를 전송하였습니다.", user)
                isPhotoSelectionOpen = false
                Log.d("image", "success")
            }
            else{
                isPhotoSelectionOpen = false
                Log.d("image", "failure")
            }
        }

    private fun recyclerview(){
        messageListAdapter = MessageListAdapter(messageViewModel, user)
        binding.messageRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = messageListAdapter
        }
    }

    private fun observer(){
        messageViewModel.messageDataList.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.messageProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.messageProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.messageProgress.hide(requireActivity())
                    messageListAdapter.submitList(state.data)

                    //메시지 마지막 아이템으로 이동
                    if (messageListAdapter.itemCount > 0) {
                        binding.messageRecyclerView.postDelayed({
                            binding.messageRecyclerView.scrollToPosition(messageListAdapter.itemCount - 1)
                        }, 100)
                    }
                }
            }
        }

        messageViewModel.sendImageLiveData.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                    binding.messageProgress.show(requireActivity())
                }
                is UiState.Failure -> {
                    binding.messageProgress.hide(requireActivity())
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.messageProgress.hide(requireActivity())
                }
            }
        }

        messageViewModel.messageNotificationLiveData.observe(viewLifecycleOwner){ state ->
            when(state){
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                }
            }
        }
    }

    override fun onPause() {
        if (!isPhotoSelectionOpen) {
            messageViewModel.removeSeenMessage(user.uid)
        }
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}