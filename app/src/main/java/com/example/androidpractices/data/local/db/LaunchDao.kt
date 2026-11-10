package com.example.androidpractices.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {

    @Query("SELECT * FROM favorite_launches")
    fun getAllFavoritesFlow(): Flow<List<LaunchEntity>>

    @Query("SELECT * FROM favorite_launches")
    suspend fun getAllFavorites(): List<LaunchEntity>

    @Query("SELECT * FROM favorite_launches WHERE id = :id")
    suspend fun getFavoriteById(id: String): LaunchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(launch: LaunchEntity): Long

    @Query("DELETE FROM favorite_launches WHERE id = :id")
    suspend fun deleteFavoriteById(id: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_launches WHERE id = :id)")
    fun isFavoriteFlow(id: String): Flow<Boolean>
}
