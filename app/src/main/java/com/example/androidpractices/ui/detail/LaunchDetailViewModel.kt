package com.example.androidpractices.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository
import com.example.androidpractices.domain.usecase.GetLaunchByIdUseCase
import com.example.androidpractices.ui.state.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val getLaunchByIdUseCase: GetLaunchByIdUseCase,
    private val launchRepository: LaunchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Launch>>(UiState.Loading)
    val uiState: StateFlow<UiState<Launch>> = _uiState.asStateFlow()

    val favoritesFlow: StateFlow<List<Launch>> = launchRepository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadLaunchDetail(launchId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val launch = getLaunchByIdUseCase(launchId)
                _uiState.value = UiState.Success(launch)
            } catch (e: Exception) {
                val fav = launchRepository.getFavoriteById(launchId)
                if (fav != null) {
                    _uiState.value = UiState.Success(fav)
                } else {
                    _uiState.value = UiState.Error(
                        e.localizedMessage ?: "Не удалось загрузить детали запуска (нет сети)."
                    )
                }
            }
        }
    }

    fun toggleFavorite(launch: Launch) {
        viewModelScope.launch {
            launchRepository.toggleFavorite(launch)
        }
    }

    class Factory(
        private val getLaunchByIdUseCase: GetLaunchByIdUseCase,
        private val launchRepository: LaunchRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LaunchDetailViewModel(getLaunchByIdUseCase, launchRepository) as T
        }
    }
}
