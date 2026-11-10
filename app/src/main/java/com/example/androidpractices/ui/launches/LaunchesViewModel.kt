package com.example.androidpractices.ui.launches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.data.local.SettingsDataStore
import com.example.androidpractices.di.FilterCache
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository
import com.example.androidpractices.domain.usecase.GetLaunchesUseCase
import com.example.androidpractices.ui.state.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LaunchesViewModel(
    private val getLaunchesUseCase: GetLaunchesUseCase,
    private val launchRepository: LaunchRepository,
    private val settingsDataStore: SettingsDataStore,
    private val filterCache: FilterCache
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Launch>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Launch>>> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val favoritesFlow: StateFlow<List<Launch>> = launchRepository.getFavoritesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            combine(
                settingsDataStore.searchQueryFlow,
                settingsDataStore.statusFilterFlow,
                settingsDataStore.limitFilterFlow
            ) { query, status, limit ->
                filterCache.updateFiltersState(query, status, limit)
            }.collect()
        }

        loadLaunchesWithSettings()
    }

    fun loadLaunchesWithSettings() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val savedQuery = settingsDataStore.searchQueryFlow.first()
                val statusFilter = settingsDataStore.statusFilterFlow.first()
                val limitFilter = settingsDataStore.limitFilterFlow.first()

                filterCache.updateFiltersState(savedQuery, statusFilter, limitFilter)

                if (_searchQuery.value.isBlank() && savedQuery.isNotBlank()) {
                    _searchQuery.value = savedQuery
                }

                val queryToUse = if (_searchQuery.value.isNotBlank()) _searchQuery.value else savedQuery
                val allLaunches = getLaunchesUseCase(queryToUse, limitFilter)

                val filtered = allLaunches.filter { launch ->
                    if (statusFilter == "Any" || statusFilter.isBlank()) true
                    else launch.status.contains(statusFilter, ignoreCase = true)
                }

                _uiState.value = UiState.Success(filtered)
            } catch (e: Exception) {
                val favs = favoritesFlow.value
                if (favs.isNotEmpty()) {
                    _uiState.value = UiState.Success(favs)
                } else {
                    _uiState.value = UiState.Error(
                        e.localizedMessage ?: "Ошибка сети. Проверьте подключение к интернету."
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
        loadLaunchesWithSettings()
    }

    fun toggleFavorite(launch: Launch) {
        viewModelScope.launch {
            launchRepository.toggleFavorite(launch)
        }
    }

    class Factory(
        private val getLaunchesUseCase: GetLaunchesUseCase,
        private val launchRepository: LaunchRepository,
        private val settingsDataStore: SettingsDataStore,
        private val filterCache: FilterCache
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LaunchesViewModel(
                getLaunchesUseCase,
                launchRepository,
                settingsDataStore,
                filterCache
            ) as T
        }
    }
}
