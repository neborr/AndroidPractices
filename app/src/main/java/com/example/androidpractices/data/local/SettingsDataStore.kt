package com.example.androidpractices.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings_preferences")

class SettingsDataStore(private val context: Context) {

    object PreferencesKeys {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
        val STATUS_FILTER = stringPreferencesKey("status_filter")
        val LIMIT_FILTER = intPreferencesKey("limit_filter")
    }

    val searchQueryFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.SEARCH_QUERY] ?: "" }

    val statusFilterFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.STATUS_FILTER] ?: "Any" }

    val limitFilterFlow: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[PreferencesKeys.LIMIT_FILTER] ?: 20 }

    suspend fun saveSettings(searchQuery: String, statusFilter: String, limitFilter: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEARCH_QUERY] = searchQuery
            preferences[PreferencesKeys.STATUS_FILTER] = statusFilter
            preferences[PreferencesKeys.LIMIT_FILTER] = limitFilter
        }
    }
}
