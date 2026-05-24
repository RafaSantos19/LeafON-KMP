package kmp.edu.leafon_kmp.presentation.pots.routines.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.model.RoutineType
import kmp.edu.leafon_kmp.data.repository.RoutineRepository
import kmp.edu.leafon_kmp.presentation.pots.RoutineErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.RoutineOperation
import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay
import kmp.edu.leafon_kmp.presentation.pots.routines.model.toApiDaysOfWeek
import kmp.edu.leafon_kmp.presentation.pots.routines.model.toWeekDaySet
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CreateRoutineViewModel(
    private val potId: String,
    private val routineId: String? = null,
    private val routineRepository: RoutineRepository,
    private val onSaved: () -> Unit = {},
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(
        CreateRoutineState(
            potId = potId,
            routineId = routineId,
            isLoading = !routineId.isNullOrBlank(),
        )
    )
        private set

    init {
        if (!routineId.isNullOrBlank()) {
            loadRoutine(routineId)
        }
    }

    fun onAction(action: CreateRoutineAction) {
        when (action) {
            is CreateRoutineAction.OnTypeChange -> updateType(action.value)
            is CreateRoutineAction.OnNameChange -> updateName(action.value)
            is CreateRoutineAction.OnTimeChange -> updateTime(action.value)
            is CreateRoutineAction.OnToggleDay -> toggleDay(action.day)
            is CreateRoutineAction.OnDurationChange -> updateDuration(action.value)
            CreateRoutineAction.OnToggleEnabled -> toggleEnabled()
            CreateRoutineAction.OnRetryLoad -> retryLoad()
            CreateRoutineAction.OnSaveClick -> saveRoutine()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun updateType(value: RoutineType) {
        state = state.copy(
            type = value,
            errorMessage = null,
        )
    }

    private fun updateName(value: String) {
        state = state.copy(
            name = value,
            errorMessage = null,
        )
    }

    private fun updateTime(value: String) {
        state = state.copy(
            time = value,
            errorMessage = null,
        )
    }

    private fun toggleDay(day: WeekDay) {
        val days = if (day in state.selectedDays) {
            state.selectedDays - day
        } else {
            state.selectedDays + day
        }

        state = state.copy(
            selectedDays = days,
            errorMessage = null,
        )
    }

    private fun updateDuration(value: String) {
        state = state.copy(
            durationInput = value
                .filter { it.isDigit() }
                .take(4),
            errorMessage = null,
        )
    }

    private fun toggleEnabled() {
        state = state.copy(
            active = !state.active,
            errorMessage = null,
        )
    }

    private fun saveRoutine() {
        val trimmedName = state.name.trim()
        val time = state.time.trim()
        val duration = state.durationInput
            .filter { it.isDigit() }
            .toIntOrNull() ?: 0

        when {
            potId.isBlank() -> {
                state = state.copy(errorMessage = "Selecione um Smart Pot valido.")
                return
            }
            state.type == RoutineType.UNKNOWN -> {
                state = state.copy(errorMessage = "Selecione um tipo de rotina valido.")
                return
            }
            trimmedName.isBlank() -> {
                state = state.copy(errorMessage = "Informe o nome da rotina.")
                return
            }
            time.isBlank() -> {
                state = state.copy(errorMessage = "Informe o horario da rotina.")
                return
            }
            !TIME_REGEX.matches(time) -> {
                state = state.copy(errorMessage = "Use o formato HH:mm:ss para o horario.")
                return
            }
            state.selectedDays.isEmpty() -> {
                state = state.copy(errorMessage = "Selecione pelo menos um dia.")
                return
            }
            duration <= 0 -> {
                state = state.copy(errorMessage = "Informe uma duracao maior que zero.")
                return
            }
        }

        scope.launch {
            state = state.copy(
                name = trimmedName,
                time = time,
                isSaving = true,
                errorMessage = null,
            )

            try {
                if (state.isEditMode) {
                    routineRepository.updateRoutine(
                        id = state.routineId.orEmpty(),
                        smartPotId = potId,
                        type = state.type,
                        name = trimmedName,
                        scheduledTime = time,
                        daysOfWeek = state.selectedDays.toApiDaysOfWeek(),
                        durationSec = duration,
                        active = state.active,
                    )
                } else {
                    routineRepository.createRoutine(
                        smartPotId = potId,
                        type = state.type,
                        name = trimmedName,
                        scheduledTime = time,
                        daysOfWeek = state.selectedDays.toApiDaysOfWeek(),
                        durationSec = duration,
                        active = state.active,
                    )
                }

                state = state.copy(isSaving = false)
                onSaved()
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isSaving = false,
                    errorMessage = RoutineErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = if (state.isEditMode) RoutineOperation.UPDATE else RoutineOperation.CREATE,
                    ),
                )
            }
        }
    }

    private fun loadRoutine(id: String) {
        scope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
            )

            try {
                val routine = routineRepository.getRoutineById(id)
                state = state.copy(
                    routineId = routine.id,
                    type = routine.type,
                    name = routine.name,
                    time = routine.scheduledTime,
                    selectedDays = routine.daysOfWeek.toWeekDaySet(),
                    durationInput = routine.durationSec.toString(),
                    active = routine.active,
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
                        operation = RoutineOperation.DETAIL,
                    ),
                )
            }
        }
    }

    private fun retryLoad() {
        val id = routineId.orEmpty()
        if (id.isNotBlank()) {
            loadRoutine(id)
        }
    }
}

private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")
