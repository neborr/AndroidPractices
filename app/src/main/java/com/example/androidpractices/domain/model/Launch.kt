package com.example.androidpractices.domain.model

data class Launch(
    val id: String,
    val name: String,
    val provider: String = "SpaceX",
    val status: String,
    val windowStart: String,
    val rocketName: String,
    val location: String,
    val description: String,
    val imageUrl: String?
)
