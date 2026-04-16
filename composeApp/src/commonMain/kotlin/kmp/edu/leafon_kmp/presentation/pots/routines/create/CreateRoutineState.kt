package kmp.edu.leafon_kmp.presentation.pots.routines.create

import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay

data class CreateRoutineState(
    val potId: String,
    val name: String = "",
    val time: String = "08:00",
    val selectedDays: Set<WeekDay> = setOf(WeekDay.MON, WeekDay.WED, WeekDay.FRI),
    val durationSec: Int = 20,
    val enabled: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)
