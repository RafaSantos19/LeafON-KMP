package kmp.edu.leafon_kmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AlertResponseDto(
    val id: String? = null,
    val smartPotId: String,
    val telemetryReadingId: String? = null,
    val type: String,
    val message: String,
    val status: String,
    val createdAt: String,
    val readAt: String? = null,
)
