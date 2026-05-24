package kmp.edu.leafon_kmp.presentation.pots.routines.create

import kmp.edu.leafon_kmp.core.model.RoutineType
import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay

data class CreateRoutineState(
    val potId: String,
    val routineId: String? = null,
    val type: RoutineType = RoutineType.IRRIGATION,
    val name: String = "",
    val time: String = "08:30:00",
    val selectedDays: Set<WeekDay> = setOf(WeekDay.MONDAY, WeekDay.WEDNESDAY, WeekDay.FRIDAY),
    val durationInput: String = "120",
    val active: Boolean = true,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
 ) {
    val isEditMode: Boolean
        get() = !routineId.isNullOrBlank()
}
