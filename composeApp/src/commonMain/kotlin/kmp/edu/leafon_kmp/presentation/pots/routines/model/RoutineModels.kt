package kmp.edu.leafon_kmp.presentation.pots.routines.model

import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.core.model.RoutineType

data class RoutineUi(
    val id: String,
    val name: String,
    val timeLabel: String,
    val daysLabel: String,
    val durationSec: Int,
    val enabled: Boolean,
)

enum class WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
}

fun WeekDay.shortLabel() = when (this) {
    WeekDay.MONDAY -> "Seg"
    WeekDay.TUESDAY -> "Ter"
    WeekDay.WEDNESDAY -> "Qua"
    WeekDay.THURSDAY -> "Qui"
    WeekDay.FRIDAY -> "Sex"
    WeekDay.SATURDAY -> "Sab"
    WeekDay.SUNDAY -> "Dom"
}

fun Set<WeekDay>.toApiDaysOfWeek(): String {
    return WeekDay.entries
        .filter { it in this }
        .joinToString(",") { it.name }
}

fun String.toWeekDaySet(): Set<WeekDay> {
    return split(',')
        .mapNotNull { value ->
            WeekDay.entries.firstOrNull { it.name == value.trim() }
        }
        .toSet()
}

fun String.toDaysLabel(): String {
    return toWeekDaySet()
        .ifEmpty { emptySet() }
        .joinToString(", ") { it.shortLabel() }
        .ifBlank { this }
}

fun RoutineType.label(): String =
    when (this) {
        RoutineType.IRRIGATION -> "Irrigacao"
        RoutineType.LIGHTING -> "Iluminacao"
        RoutineType.UNKNOWN -> "Tipo desconhecido"
    }

fun Routine.statusLabel(): String = if (active) "Ativa" else "Inativa"
