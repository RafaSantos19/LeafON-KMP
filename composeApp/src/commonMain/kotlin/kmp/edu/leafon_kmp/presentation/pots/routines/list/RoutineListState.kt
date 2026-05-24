package kmp.edu.leafon_kmp.presentation.pots.routines.list

import kmp.edu.leafon_kmp.core.model.Routine

data class RoutineListState(
    val potId: String,
    val routines: List<Routine> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val busyRoutineId: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)
