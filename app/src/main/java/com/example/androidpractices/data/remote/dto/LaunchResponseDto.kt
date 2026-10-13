package com.example.androidpractices.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LaunchResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<LaunchDto>
)