package com.example.androidpractices.data.repository

import com.example.androidpractices.data.remote.LaunchLibraryApi
import com.example.androidpractices.data.remote.dto.LaunchDto
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class LaunchRepositoryImpl(
    private val api: LaunchLibraryApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LaunchRepository {

    private val cacheFlow = MutableStateFlow<List<Launch>>(emptyList())

    override fun getLaunchesFlow(): Flow<List<Launch>> = cacheFlow.asStateFlow()

    override suspend fun fetchLaunches(): List<Launch> = withContext(ioDispatcher) {
        val response = api.getLaunches()
        val launches = response.results.map { it.toDomainModel() }
        cacheFlow.value = launches
        launches
    }

    override suspend fun getLaunchById(id: String): Launch = withContext(ioDispatcher) {
        cacheFlow.value.find { it.id == id } ?: api.getLaunchById(id).toDomainModel()
    }

    private fun LaunchDto.toDomainModel(): Launch {
        return Launch(
            id = id,
            name = name,
            provider = provider?.name ?: "Unknown Provider",
            status = status?.name ?: "Запланирован",
            windowStart = net?.take(10) ?: "Дата неизвестна",
            rocketName = rocket?.configuration?.name ?: "Rocket",
            location = pad?.name ?: pad?.location?.name ?: "Spaceport",
            description = mission?.description ?: "Информация о деталях запуска отсутствует.",
            imageUrl = image
        )
    }
}