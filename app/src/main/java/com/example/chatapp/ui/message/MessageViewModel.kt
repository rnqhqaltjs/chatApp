package com.example.chatapp.ui.message

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.model.Message
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: ChatRepository
): ViewModel() {

    private val _messageDataList = MutableLiveData<UiState<List<Message>>>()
    val messageDataList: LiveData<UiState<List<Message>>>
        get() = _messageDataList

    private val _sendImageLiveData = MutableLiveData<UiState<String>>()
    val sendImageLiveData: LiveData<UiState<String>>
        get() = _sendImageLiveData

    private val _messageNotificationLiveData = MutableLiveData<UiState<String>>()
    val messageNotificationLiveData: LiveData<UiState<String>>
        get() = _messageNotificationLiveData

    fun sendMessage(message:String, receiverUid: String, time: String, userReceiver: User) = viewModelScope.launch {
        repository.sendMessage(message, receiverUid, time, userReceiver)
    }

    fun sendImageMessage(message: String, image: ByteArray?, receiverUid: String, time: String, userReceiver: User) = viewModelScope.launch {
        _sendImageLiveData.value = UiState.Loading
        repository.sendImageMessage(message, image, receiverUid, time, userReceiver){
            _sendImageLiveData.value = it
        }
    }

    fun getMessageData(receiverUid:String) = viewModelScope.launch {
        _messageDataList.value = UiState.Loading
        repository.getMessageData(receiverUid){
            _messageDataList.value = it
        }
    }

    fun sendMessageNotification(message:String, userReceiver: User) = viewModelScope.launch {
        _messageNotificationLiveData.value = UiState.Loading
        repository.sendMessageNotification(message, userReceiver){
            _messageNotificationLiveData.value = it
        }
    }

    fun removeSeenMessage(receiverUid: String) {
        repository.removeSeenMessage(receiverUid)
    }

    val sendImageBtnVisibility = ObservableInt(View.VISIBLE)
    val sendBtnVisibility = ObservableInt(View.INVISIBLE)

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isEmpty()) {
            sendImageBtnVisibility.set(View.VISIBLE)
            sendBtnVisibility.set(View.INVISIBLE)
        } else {
            sendImageBtnVisibility.set(View.INVISIBLE)
            sendBtnVisibility.set(View.VISIBLE)
        }
    }

}