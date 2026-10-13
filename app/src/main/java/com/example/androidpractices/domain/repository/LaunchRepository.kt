package com.example.androidpractices.domain.repository

import com.example.androidpractices.domain.model.Launch
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    fun getLaunchesFlow(): Flow<List<Launch>>
    suspend fun fetchLaunches(): List<Launch>
    suspend fun getLaunchById(id: String): Launch
}
