package kmp.edu.leafon_kmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SmartPotResponseDto(
    val id: String? = null,
    val plantName: String,
    val humidityMin: Int,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

@Serializable
data class CreateSmartPotRequestDto(
    val plantName: String,
    val humidityMin: Int,
    val deviceId: String? = null,
)

@Serializable
data class UpdateSmartPotRequestDto(
    val plantName: String,
    val humidityMin: Int,
    val deviceId: String? = null,
)
