package com.example.androidpractices.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.usecase.GetLaunchByIdUseCase
import com.example.androidpractices.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaunchDetailViewModel(
    private val getLaunchByIdUseCase: GetLaunchByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Launch>>(UiState.Loading)
    val uiState: StateFlow<UiState<Launch>> = _uiState.asStateFlow()

    fun loadLaunchDetail(launchId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val launch = getLaunchByIdUseCase(launchId)
                _uiState.value = UiState.Success(launch)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.localizedMessage ?: "Не удалось загрузить детали запуска."
                )
            }
        }
    }

    class Factory(
        private val getLaunchByIdUseCase: GetLaunchByIdUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LaunchDetailViewModel(getLaunchByIdUseCase) as T
        }
    }
}
