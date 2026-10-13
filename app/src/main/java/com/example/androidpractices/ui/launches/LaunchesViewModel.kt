package com.example.androidpractices.ui.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.usecase.GetLaunchesUseCase
import com.example.androidpractices.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LaunchesViewModel(
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Launch>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Launch>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadLaunches()
    }

    fun loadLaunches(query: String = _searchQuery.value) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = getLaunchesUseCase(query)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.localizedMessage ?: "Ошибка при загрузке данных. Проверьте подключение к сети."
                )
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        loadLaunches(newQuery)
    }

    class Factory(
        private val getLaunchesUseCase: GetLaunchesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LaunchesViewModel(getLaunchesUseCase) as T
        }
    }
}
