package com.example.androidpractices.feature.profile.ui

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.feature.profile.data.local.ProfileDataStore
import com.example.androidpractices.feature.profile.domain.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    val profile: StateFlow<UserProfile> = profileDataStore.profileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    fun openResume(context: Context, url: String) {
        if (url.isBlank()) return
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Factory(
        private val profileDataStore: ProfileDataStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileDataStore) as T
        }
    }
}
