package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.Launch
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    fun getLaunchesFlow(): Flow<List<Launch>>
    suspend fun fetchLaunches(limit: Int = 20, search: String? = null): List<Launch>
    suspend fun getLaunchById(id: String): Launch
    fun getFavoritesFlow(): Flow<List<Launch>>
    suspend fun toggleFavorite(launch: Launch)
    fun isFavoriteFlow(id: String): Flow<Boolean>
    suspend fun getFavoriteById(id: String): Launch?
}
