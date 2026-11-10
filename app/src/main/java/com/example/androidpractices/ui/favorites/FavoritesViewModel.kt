package com.example.androidpractices.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val launchRepository: LaunchRepository
) : ViewModel() {

    val favorites: StateFlow<List<Launch>> = launchRepository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(launch: Launch) {
        viewModelScope.launch {
            launchRepository.toggleFavorite(launch)
        }
    }

    class Factory(
        private val launchRepository: LaunchRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(launchRepository) as T
        }
    }
}
