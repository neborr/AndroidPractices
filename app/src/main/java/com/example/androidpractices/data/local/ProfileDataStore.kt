package com.example.androidpractices.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.androidpractices.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.profileDataStore by preferencesDataStore(name = "profile_preferences")

class ProfileDataStore(private val context: Context) {

    object PreferencesKeys {
        val FULL_NAME = stringPreferencesKey("full_name")
        val AVATAR_URI = stringPreferencesKey("avatar_uri")
        val RESUME_URL = stringPreferencesKey("resume_url")
        val POSITION = stringPreferencesKey("position")
    }

    val profileFlow: Flow<UserProfile> = context.profileDataStore.data
        .map { preferences ->
            UserProfile(
                fullName = preferences[PreferencesKeys.FULL_NAME] ?: "",
                avatarUri = preferences[PreferencesKeys.AVATAR_URI],
                resumeUrl = preferences[PreferencesKeys.RESUME_URL] ?: "",
                position = preferences[PreferencesKeys.POSITION] ?: "Space Explorer & Developer"
            )
        }

    suspend fun saveProfile(profile: UserProfile) {
        context.profileDataStore.edit { preferences ->
            preferences[PreferencesKeys.FULL_NAME] = profile.fullName
            if (profile.avatarUri != null) {
                preferences[PreferencesKeys.AVATAR_URI] = profile.avatarUri
            } else {
                preferences.remove(PreferencesKeys.AVATAR_URI)
            }
            preferences[PreferencesKeys.RESUME_URL] = profile.resumeUrl
            preferences[PreferencesKeys.POSITION] = profile.position
        }
    }
}
