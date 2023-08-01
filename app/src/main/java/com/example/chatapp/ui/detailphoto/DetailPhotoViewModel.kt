package com.example.chatapp.ui.detailphoto

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.chatapp.data.model.User
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPhotoViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    fun exitPhotoView(view: View) {
        view.findNavController().popBackStack()
    }

    val switch = ObservableBoolean(false)

    fun onDetailPhotoImageClick(currentSwitchState: Boolean) {
        switch.set(!currentSwitchState)
    }
}