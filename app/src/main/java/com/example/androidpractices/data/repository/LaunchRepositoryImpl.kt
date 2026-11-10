package com.example.androidpractices.data.repository

import com.example.androidpractices.data.local.db.LaunchDao
import com.example.androidpractices.data.local.db.toDomainModel
import com.example.androidpractices.data.local.db.toEntity
import com.example.androidpractices.data.remote.LaunchLibraryApi
import com.example.androidpractices.data.remote.dto.LaunchDto
import com.example.androidpractices.domain.model.Launch
import com.example.androidpractices.domain.repository.LaunchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LaunchRepositoryImpl(
    private val api: LaunchLibraryApi,
    private val launchDao: LaunchDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LaunchRepository {

    private val cacheFlow = MutableStateFlow<List<Launch>>(emptyList())

    override fun getLaunchesFlow(): Flow<List<Launch>> = cacheFlow.asStateFlow()

    override suspend fun fetchLaunches(limit: Int, search: String?): List<Launch> = withContext(ioDispatcher) {
        val response = api.getLaunches(limit = limit, search = search)
        val launches = response.results.map { it.toDomainModel() }
        cacheFlow.value = launches
        launches
    }

    override suspend fun getLaunchById(id: String): Launch = withContext(ioDispatcher) {
        val cached = cacheFlow.value.find { it.id == id }
        if (cached != null) return@withContext cached

        val favorite = launchDao.getFavoriteById(id)?.toDomainModel()
        if (favorite != null) return@withContext favorite

        api.getLaunchById(id).toDomainModel()
    }

    override fun getFavoritesFlow(): Flow<List<Launch>> {
        return launchDao.getAllFavoritesFlow().map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun toggleFavorite(launch: Launch): Unit = withContext(ioDispatcher) {
        val existing = launchDao.getFavoriteById(launch.id)
        if (existing != null) {
            launchDao.deleteFavoriteById(launch.id)
        } else {
            launchDao.insertFavorite(launch.toEntity())
        }
        Unit
    }

    override fun isFavoriteFlow(id: String): Flow<Boolean> {
        return launchDao.isFavoriteFlow(id)
    }

    override suspend fun getFavoriteById(id: String): Launch? = withContext(ioDispatcher) {
        launchDao.getFavoriteById(id)?.toDomainModel()
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
