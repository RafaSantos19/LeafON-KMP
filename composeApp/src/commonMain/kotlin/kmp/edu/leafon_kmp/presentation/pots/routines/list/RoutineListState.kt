package kmp.edu.leafon_kmp.presentation.pots.routines.list

import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

data class RoutineListState(
    val potId: String,
    val routines: List<RoutineUi> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
