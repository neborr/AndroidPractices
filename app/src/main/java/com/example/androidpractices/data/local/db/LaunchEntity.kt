package com.example.androidpractices.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidpractices.domain.model.Launch

@Entity(tableName = "favorite_launches")
data class LaunchEntity(
    @field:PrimaryKey val id: String,
    val name: String,
    val provider: String,
    val status: String,
    val windowStart: String,
    val rocketName: String,
    val location: String,
    val description: String,
    val imageUrl: String?
)

fun LaunchEntity.toDomainModel(): Launch {
    return Launch(
        id = id,
        name = name,
        provider = provider,
        status = status,
        windowStart = windowStart,
        rocketName = rocketName,
        location = location,
        description = description,
        imageUrl = imageUrl
    )
}

fun Launch.toEntity(): LaunchEntity {
    return LaunchEntity(
        id = id,
        name = name,
        provider = provider,
        status = status,
        windowStart = windowStart,
        rocketName = rocketName,
        location = location,
        description = description,
        imageUrl = imageUrl
    )
}
