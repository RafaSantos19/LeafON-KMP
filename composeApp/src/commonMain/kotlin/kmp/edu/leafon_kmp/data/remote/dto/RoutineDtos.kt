package kmp.edu.leafon_kmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RoutineResponseDto(
    val id: String? = null,
    val smartPotId: String,
    val type: String,
    val name: String,
    val scheduledTime: String,
    val daysOfWeek: String,
    val durationSec: Int,
    val active: Boolean,
    val lastExecutedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

@Serializable
data class CreateRoutineRequestDto(
    val smartPotId: String,
    val type: String,
    val name: String,
    val scheduledTime: String,
    val daysOfWeek: String,
    val durationSec: Int,
    val active: Boolean,
)

@Serializable
data class UpdateRoutineRequestDto(
    val smartPotId: String,
    val type: String,
    val name: String,
    val scheduledTime: String,
    val daysOfWeek: String,
    val durationSec: Int,
    val active: Boolean,
)
