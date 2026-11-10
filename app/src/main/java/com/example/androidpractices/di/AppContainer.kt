package com.example.androidpractices.di

import android.content.Context
import com.example.androidpractices.data.local.ProfileDataStore
import com.example.androidpractices.data.local.SettingsDataStore
import com.example.androidpractices.data.local.db.AppDatabase
import com.example.androidpractices.data.local.db.LaunchDao
import com.example.androidpractices.data.remote.LaunchLibraryApi
import com.example.androidpractices.data.remote.RetrofitClient
import com.example.androidpractices.data.repository.LaunchRepositoryImpl
import com.example.androidpractices.domain.repository.LaunchRepository
import com.example.androidpractices.domain.usecase.GetLaunchByIdUseCase
import com.example.androidpractices.domain.usecase.GetLaunchesUseCase

interface AppContainer {
    val settingsDataStore: SettingsDataStore
    val profileDataStore: ProfileDataStore
    val filterCache: FilterCache
    val launchDao: LaunchDao
    val launchRepository: LaunchRepository
    val getLaunchesUseCase: GetLaunchesUseCase
    val getLaunchByIdUseCase: GetLaunchByIdUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val settingsDataStore: SettingsDataStore by lazy {
        SettingsDataStore(context)
    }

    override val profileDataStore: ProfileDataStore by lazy {
        ProfileDataStore(context)
    }

    override val filterCache: FilterCache by lazy {
        FilterCache()
    }

    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    override val launchDao: LaunchDao by lazy {
        database.launchDao()
    }

    private val api: LaunchLibraryApi by lazy {
        RetrofitClient.api
    }

    override val launchRepository: LaunchRepository by lazy {
        LaunchRepositoryImpl(api, launchDao)
    }

    override val getLaunchesUseCase: GetLaunchesUseCase by lazy {
        GetLaunchesUseCase(launchRepository)
    }

    override val getLaunchByIdUseCase: GetLaunchByIdUseCase by lazy {
        GetLaunchByIdUseCase(launchRepository)
    }
}
