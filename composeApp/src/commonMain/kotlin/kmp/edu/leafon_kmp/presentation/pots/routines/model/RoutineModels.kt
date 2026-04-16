package kmp.edu.leafon_kmp.presentation.pots.routines.model

data class RoutineUi(
    val id: String,
    val name: String,
    val timeLabel: String,
    val daysLabel: String,
    val durationSec: Int,
    val enabled: Boolean,
)

enum class WeekDay {
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT,
    SUN,
}

fun WeekDay.shortLabel() = when (this) {
    WeekDay.MON -> "Seg"
    WeekDay.TUE -> "Ter"
    WeekDay.WED -> "Qua"
    WeekDay.THU -> "Qui"
    WeekDay.FRI -> "Sex"
    WeekDay.SAT -> "Sab"
    WeekDay.SUN -> "Dom"
}

fun Set<WeekDay>.toDaysLabel(): String {
    return WeekDay.entries
        .filter { it in this }
        .joinToString(", ") { it.shortLabel() }
}
