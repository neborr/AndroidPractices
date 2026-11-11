package com.example.androidpractices.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.androidpractices.data.local.SettingsDataStore
import com.example.androidpractices.di.FilterCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val filterCache: FilterCache
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("Any")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    private val _limitFilter = MutableStateFlow(20)
    val limitFilter: StateFlow<Int> = _limitFilter.asStateFlow()

    init {
        viewModelScope.launch {
            val query = settingsDataStore.searchQueryFlow.first()
            val status = settingsDataStore.statusFilterFlow.first()
            val limit = settingsDataStore.limitFilterFlow.first()
            _searchQuery.value = query
            _statusFilter.value = status
            _limitFilter.value = limit
            filterCache.updateFiltersState(query, status, limit)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChanged(status: String) {
        _statusFilter.value = status
    }

    fun onLimitFilterChanged(limit: Int) {
        _limitFilter.value = limit
    }

    fun saveSettings(onSaved: () -> Unit) {
        viewModelScope.launch {
            settingsDataStore.saveSettings(_searchQuery.value, _statusFilter.value, _limitFilter.value)
            filterCache.updateFiltersState(_searchQuery.value, _statusFilter.value, _limitFilter.value)
            onSaved()
        }
    }

    fun resetSettings(onReset: () -> Unit) {
        viewModelScope.launch {
            _searchQuery.value = ""
            _statusFilter.value = "Any"
            _limitFilter.value = 20
            settingsDataStore.saveSettings("", "Any", 20)
            filterCache.updateFiltersState("", "Any", 20)
            onReset()
        }
    }

    class Factory(
        private val settingsDataStore: SettingsDataStore,
        private val filterCache: FilterCache
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(settingsDataStore, filterCache) as T
        }
    }
}
