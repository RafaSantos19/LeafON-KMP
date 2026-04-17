package kmp.edu.leafon_kmp.presentation.pots.routines.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria

class RoutineListViewModel(
    private val potId: String,
    private val repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
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
            routines = repositorio.listarRotinas(potId),
            isLoading = false,
            errorMessage = null,
        )
    }

    private fun toggleRoutine(id: String) {
        val routine = state.routines.firstOrNull { it.id == id } ?: return

        repositorio.atualizarRotina(
            potId = potId,
            rotina = routine.copy(enabled = !routine.enabled),
        )

        loadRoutines()
    }
}
