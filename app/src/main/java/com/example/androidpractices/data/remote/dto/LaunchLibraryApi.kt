package com.example.androidpractices.data.remote

import com.example.androidpractices.data.remote.dto.LaunchDto
import com.example.androidpractices.data.remote.dto.LaunchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LaunchLibraryApi {

    @GET("launch/")
    suspend fun getLaunches(
        @Query("limit") limit: Int? = 20,
        @Query("search") search: String? = null
    ): LaunchResponseDto

    @GET("launch/{id}/")
    suspend fun getLaunchById(
        @Path("id") id: String
    ): LaunchDto
}