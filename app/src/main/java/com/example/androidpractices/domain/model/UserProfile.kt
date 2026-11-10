package com.example.androidpractices.domain.model

data class UserProfile(
    val fullName: String = "",
    val avatarUri: String? = null,
    val resumeUrl: String = "",
    val position: String = "Space Explorer & Developer"
)
