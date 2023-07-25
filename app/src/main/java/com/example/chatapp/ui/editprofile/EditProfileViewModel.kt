package com.example.chatapp.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.ChatRepository
import com.example.chatapp.data.repository.MenuRepository
import com.example.chatapp.util.SingleLiveEvent
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: MenuRepository
): ViewModel() {

    private val _profileLiveData = MutableLiveData<UiState<String>>()
    val profileLiveData: LiveData<UiState<String>>
        get() = _profileLiveData

    private val _profileUpdateLiveData = SingleLiveEvent<UiState<String>>()
    val profileUpdateLiveData: LiveData<UiState<String>>
        get() = _profileUpdateLiveData

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit)) = viewModelScope.launch {
        _profileLiveData.value = UiState.Loading
        repository.getProfileData(image, name, email){
            _profileLiveData.value = it
        }
    }

    fun profileChange(name: String, image: ByteArray?) = viewModelScope.launch {
        _profileUpdateLiveData.value = UiState.Loading
        repository.profileChange(name, image){
            _profileUpdateLiveData.value = it
        }
    }

}