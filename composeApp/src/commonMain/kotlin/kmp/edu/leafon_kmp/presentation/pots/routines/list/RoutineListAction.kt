package kmp.edu.leafon_kmp.presentation.pots.routines.list

sealed interface RoutineListAction {
    data class OnToggleRoutine(val id: String) : RoutineListAction
    data object OnCreateRoutineClick : RoutineListAction
    data object OnRetryClick : RoutineListAction
}
