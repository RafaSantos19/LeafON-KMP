package kmp.edu.leafon_kmp.presentation.pots.routines.list

sealed interface RoutineListAction {
    data class OnActivateRoutine(val id: String) : RoutineListAction
    data class OnDeactivateRoutine(val id: String) : RoutineListAction
    data class OnSimulateRoutine(val id: String) : RoutineListAction
    data class OnDeleteRoutine(val id: String) : RoutineListAction
    data object OnCreateRoutineClick : RoutineListAction
    data object OnRetryClick : RoutineListAction
}
