package com.example.androidpractices.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LaunchDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("net") val net: String?, // Дата и время старта
    @SerializedName("status") val status: LaunchStatusDto?,
    @SerializedName("launch_service_provider") val provider: LaunchServiceProviderDto?,
    @SerializedName("rocket") val rocket: RocketDto?,
    @SerializedName("pad") val pad: PadDto?,
    @SerializedName("mission") val mission: MissionDto?,
    @SerializedName("image") val image: String?
)

data class LaunchStatusDto(
    @SerializedName("name") val name: String?,
    @SerializedName("abbrev") val abbrev: String?
)

data class LaunchServiceProviderDto(
    @SerializedName("name") val name: String?
)

data class RocketDto(
    @SerializedName("configuration") val configuration: RocketConfigurationDto?
)

data class RocketConfigurationDto(
    @SerializedName("name") val name: String?
)

data class PadDto(
    @SerializedName("name") val name: String?,
    @SerializedName("location") val location: LocationDto?
)

data class LocationDto(
    @SerializedName("name") val name: String?
)

data class MissionDto(
    @SerializedName("description") val description: String?
)