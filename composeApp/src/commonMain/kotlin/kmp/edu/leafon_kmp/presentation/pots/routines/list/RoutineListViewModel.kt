package kmp.edu.leafon_kmp.presentation.pots.routines.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.data.repository.RoutineRepository
import kmp.edu.leafon_kmp.presentation.pots.RoutineErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.RoutineOperation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RoutineListViewModel(
    private val potId: String,
    private val routineRepository: RoutineRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(RoutineListState(potId = potId))
        private set

    init {
        loadRoutines()
    }

    fun onAction(action: RoutineListAction) {
        when (action) {
            is RoutineListAction.OnActivateRoutine -> activateRoutine(action.id)
            is RoutineListAction.OnDeactivateRoutine -> deactivateRoutine(action.id)
            is RoutineListAction.OnSimulateRoutine -> simulateRoutine(action.id)
            is RoutineListAction.OnDeleteRoutine -> deleteRoutine(action.id)
            RoutineListAction.OnCreateRoutineClick -> Unit
            RoutineListAction.OnRetryClick -> loadRoutines()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadRoutines() {
        scope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

            try {
                state = state.copy(
                    routines = routineRepository.listRoutines().filterByPotId(),
                    isLoading = false,
                    errorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = RoutineErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = RoutineOperation.LIST,
                    ),
                )
            }
        }
    }

    private fun activateRoutine(id: String) {
        updateRoutine(
            id = id,
            operation = RoutineOperation.ACTIVATE,
            successMessage = "Rotina ativada com sucesso.",
            call = { routineRepository.activateRoutine(id) },
        )
    }

    private fun deactivateRoutine(id: String) {
        updateRoutine(
            id = id,
            operation = RoutineOperation.DEACTIVATE,
            successMessage = "Rotina desativada com sucesso.",
            call = { routineRepository.deactivateRoutine(id) },
        )
    }

    private fun simulateRoutine(id: String) {
        updateRoutine(
            id = id,
            operation = RoutineOperation.SIMULATE,
            successMessage = "Execucao simulada registrada.",
            call = { routineRepository.simulateExecution(id) },
        )
    }

    private fun deleteRoutine(id: String) {
        scope.launch {
            state = state.copy(
                isDeleting = true,
                busyRoutineId = id,
                errorMessage = null,
                successMessage = null,
            )

            try {
                routineRepository.deleteRoutine(id)
                state = state.copy(
                    routines = state.routines.filterNot { it.id == id },
                    isDeleting = false,
                    busyRoutineId = null,
                    successMessage = "Rotina excluida com sucesso.",
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isDeleting = false,
                    busyRoutineId = null,
                    errorMessage = RoutineErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = RoutineOperation.DELETE,
                    ),
                )
            }
        }
    }

    private fun updateRoutine(
        id: String,
        operation: RoutineOperation,
        successMessage: String,
        call: suspend () -> Routine,
    ) {
        scope.launch {
            state = state.copy(
                isSaving = true,
                busyRoutineId = id,
                errorMessage = null,
                successMessage = null,
            )

            try {
                val updated = call()
                state = state.copy(
                    routines = state.routines.map { current ->
                        if (current.id == updated.id) updated else current
                    }.filterByPotId(),
                    isSaving = false,
                    busyRoutineId = null,
                    successMessage = successMessage,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isSaving = false,
                    busyRoutineId = null,
                    errorMessage = RoutineErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = operation,
                    ),
                )
            }
        }
    }

    private fun List<Routine>.filterByPotId(): List<Routine> {
        val resolvedPotId = potId.trim()
        return if (resolvedPotId.isBlank()) {
            this
        } else {
            filter { it.smartPotId == resolvedPotId }
        }
    }
}
