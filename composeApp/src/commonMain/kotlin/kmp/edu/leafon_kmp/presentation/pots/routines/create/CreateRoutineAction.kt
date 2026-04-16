package kmp.edu.leafon_kmp.presentation.pots.routines.create

import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay

sealed interface CreateRoutineAction {
    data class OnNameChange(val value: String) : CreateRoutineAction
    data class OnTimeChange(val value: String) : CreateRoutineAction
    data class OnToggleDay(val day: WeekDay) : CreateRoutineAction
    data class OnDurationChange(val value: String) : CreateRoutineAction
    data object OnToggleEnabled : CreateRoutineAction
    data object OnSaveClick : CreateRoutineAction
}
