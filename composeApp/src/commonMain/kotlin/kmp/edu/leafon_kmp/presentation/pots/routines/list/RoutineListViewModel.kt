package kmp.edu.leafon_kmp.presentation.pots.routines.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

class RoutineListViewModel(
    private val potId: String,
) {

    var state by mutableStateOf(RoutineListState(potId = potId))
        private set

    init {
        loadRoutines()
    }

    fun onAction(action: RoutineListAction) {
        when (action) {
            is RoutineListAction.OnToggleRoutine -> toggleRoutine(action.id)
            RoutineListAction.OnCreateRoutineClick -> Unit
            RoutineListAction.OnRetryClick -> loadRoutines()
        }
    }

    private fun loadRoutines() {
        state = state.copy(
            routines = fakeRoutinesForPot(potId),
            isLoading = false,
            errorMessage = null,
        )
    }

    private fun toggleRoutine(id: String) {
        state = state.copy(
            routines = state.routines.map { routine ->
                if (routine.id == id) {
                    routine.copy(enabled = !routine.enabled)
                } else {
                    routine
                }
            },
        )
    }
}

private fun fakeRoutinesForPot(potId: String): List<RoutineUi> {
    return when (potId) {
        "3" -> listOf(
            RoutineUi(
                id = "$potId-low-water",
                name = "Reforco do cacto",
                timeLabel = "07:30",
                daysLabel = "Seg, Sex",
                durationSec = 12,
                enabled = true,
            ),
            RoutineUi(
                id = "$potId-weekend",
                name = "Fim de semana",
                timeLabel = "09:00",
                daysLabel = "Sab",
                durationSec = 8,
                enabled = false,
            ),
        )
        "4" -> listOf(
            RoutineUi(
                id = "$potId-recovery",
                name = "Recuperacao",
                timeLabel = "08:15",
                daysLabel = "Ter, Qui, Sab",
                durationSec = 25,
                enabled = false,
            ),
        )
        else -> listOf(
            RoutineUi(
                id = "$potId-morning",
                name = "Irrigacao matinal",
                timeLabel = "08:00",
                daysLabel = "Seg, Qua, Sex",
                durationSec = 20,
                enabled = true,
            ),
            RoutineUi(
                id = "$potId-evening",
                name = "Revisao da tarde",
                timeLabel = "18:30",
                daysLabel = "Ter, Qui",
                durationSec = 15,
                enabled = true,
            ),
        )
    }
}
