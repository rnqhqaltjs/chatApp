package com.example.chatapp.ui.message

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.data.model.User
import com.example.chatapp.databinding.FragmentMessageBinding
import com.example.chatapp.ui.activity.HomeActivity
import com.example.chatapp.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment() {
    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MessageFragmentArgs>()
    lateinit var messageListAdapter: MessageListAdapter

    private val chatViewModel by viewModels<MessageViewModel>()
    private val time = System.currentTimeMillis()

    private lateinit var imageUri: Uri
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = args.user
        (activity as HomeActivity).supportActionBar?.title = user.name

        recyclerview()

        chatViewModel.getMessageData(user.uid)
        observer()

        binding.messageEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트 변경 전에 수행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중에 수행할 작업
                if (s.isNullOrEmpty()) {
                    binding.sendImageBtn.visibility = View.VISIBLE
                    binding.sendBtn.visibility = View.INVISIBLE
                } else {
                    binding.sendImageBtn.visibility = View.INVISIBLE
                    binding.sendBtn.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후에 수행할 작업
            }
        })

        binding.sendImageBtn.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        binding.sendBtn.setOnClickListener {
            if(binding.messageEdit.text.toString().isNotEmpty()){
                chatViewModel.sendMessage(
                    binding.messageEdit.text.toString(),
                    user.uid,
                    time.toString(),
                    user
                )
                chatViewModel.sendNotification(binding.messageEdit.text.toString(), user)
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

                chatViewModel.sendImageMessage(
                    "이미지를 전송하였습니다.",
                    convertFileToByteArray(requireContext(),imageUri),
                    user.uid,time.toString(),
                    user
                )
                Log.d("image", "success")
            }
            else{
                Log.d("image", "failure")
            }
        }

    private fun recyclerview(){
        messageListAdapter = MessageListAdapter(chatViewModel, user)
        binding.messageRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = messageListAdapter
        }
    }

    private fun observer(){
        chatViewModel.messageobserve.observe(viewLifecycleOwner){ state ->
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
                            binding.messageRecyclerView.smoothScrollToPosition(messageListAdapter.itemCount - 1)
                        }, 100)
                    }
                }
            }
        }

        chatViewModel.notifyobserve.observe(viewLifecycleOwner){ state ->
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
        chatViewModel.removeSeenMessage(user.uid)
        super.onPause()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}