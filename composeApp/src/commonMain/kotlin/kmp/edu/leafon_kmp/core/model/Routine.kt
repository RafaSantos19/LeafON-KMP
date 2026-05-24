package kmp.edu.leafon_kmp.core.model

data class Routine(
    val id: String,
    val smartPotId: String,
    val type: RoutineType,
    val name: String,
    val scheduledTime: String,
    val daysOfWeek: String,
    val durationSec: Int,
    val active: Boolean,
    val lastExecutedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

enum class RoutineType {
    IRRIGATION,
    LIGHTING,
    UNKNOWN,
}
