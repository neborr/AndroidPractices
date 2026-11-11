package com.example.androidpractices

import android.app.Application
import com.example.androidpractices.di.AppContainer
import com.example.androidpractices.di.DefaultAppContainer

class AndroidPracticesApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
