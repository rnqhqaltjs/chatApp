package com.example.chatapp.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.data.repository.MenuRepository
import com.example.chatapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: MenuRepository
): ViewModel() {

    private val _profileLiveData = MutableLiveData<UiState<String>>()
    val profileLiveData: LiveData<UiState<String>>
        get() = _profileLiveData

    fun getProfileData(image: ((String)->Unit), name: ((String)->Unit), email: ((String)->Unit)) = viewModelScope.launch {
        _profileLiveData.value = UiState.Loading
        repository.getProfileData(image, name, email){
            _profileLiveData.value = it
        }
    }

    fun logout(){
        repository.logout()
    }
}