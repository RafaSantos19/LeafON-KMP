package kmp.edu.leafon_kmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TelemetryResponseDto(
    val id: String? = null,
    val smartPotId: String? = null,
    val soilHumidity: Int,
    val temperature: Double,
    val luminosity: Double,
    val readAt: String,
    val createdAt: String? = null,
)

@Serializable
data class CreateTelemetryRequestDto(
    val smartPotId: String,
    val soilHumidity: Int,
    val temperature: Double,
    val luminosity: Double,
    val readAt: String,
)
