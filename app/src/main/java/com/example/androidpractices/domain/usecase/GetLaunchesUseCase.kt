package com.example.androidpractices.domain.usecase

import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository

class GetLaunchesUseCase(
    private val repository: LaunchRepository
) {
    suspend operator fun invoke(query: String = "", limit: Int = 20): List<Launch> {
        val searchQuery = query.trim().ifEmpty { null }
        val launches = repository.fetchLaunches(limit = limit, search = searchQuery)
        return if (query.isBlank()) {
            launches
        } else {
            launches.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.status.contains(query, ignoreCase = true) ||
                it.provider.contains(query, ignoreCase = true)
            }
        }
    }
}
