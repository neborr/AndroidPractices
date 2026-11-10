package com.example.androidpractices.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.data.local.ProfileDataStore
import com.example.androidpractices.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _position = MutableStateFlow("")
    val position: StateFlow<String> = _position.asStateFlow()

    private val _resumeUrl = MutableStateFlow("")
    val resumeUrl: StateFlow<String> = _resumeUrl.asStateFlow()

    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri: StateFlow<String?> = _avatarUri.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = profileDataStore.profileFlow.first()
            _fullName.value = profile.fullName
            _position.value = profile.position
            _resumeUrl.value = profile.resumeUrl
            _avatarUri.value = profile.avatarUri
        }
    }

    fun onFullNameChanged(name: String) {
        _fullName.value = name
    }

    fun onPositionChanged(pos: String) {
        _position.value = pos
    }

    fun onResumeUrlChanged(url: String) {
        _resumeUrl.value = url
    }

    fun onAvatarUriChanged(uri: String?) {
        _avatarUri.value = uri
    }

    fun saveProfile(onSaved: () -> Unit) {
        viewModelScope.launch {
            val updated = UserProfile(
                fullName = _fullName.value,
                position = _position.value,
                resumeUrl = _resumeUrl.value,
                avatarUri = _avatarUri.value
            )
            profileDataStore.saveProfile(updated)
            onSaved()
        }
    }

    class Factory(
        private val profileDataStore: ProfileDataStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EditProfileViewModel(profileDataStore) as T
        }
    }
}
