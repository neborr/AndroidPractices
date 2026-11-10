package com.example.androidpractices.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterCache {
    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    fun updateFiltersState(searchQuery: String, statusFilter: String, limitFilter: Int) {
        val isActive = searchQuery.isNotBlank() || (statusFilter != "Any" && statusFilter.isNotBlank()) || limitFilter != 20
        _hasActiveFilters.value = isActive
    }
}
