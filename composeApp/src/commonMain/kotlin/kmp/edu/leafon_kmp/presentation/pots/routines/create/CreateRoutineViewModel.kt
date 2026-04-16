package kmp.edu.leafon_kmp.presentation.pots.routines.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.presentation.pots.routines.model.WeekDay

class CreateRoutineViewModel(
    potId: String,
    private val onCreated: () -> Unit = {},
) {

    var state by mutableStateOf(CreateRoutineState(potId = potId))
        private set

    fun onAction(action: CreateRoutineAction) {
        when (action) {
            is CreateRoutineAction.OnNameChange -> updateName(action.value)
            is CreateRoutineAction.OnTimeChange -> updateTime(action.value)
            is CreateRoutineAction.OnToggleDay -> toggleDay(action.day)
            is CreateRoutineAction.OnDurationChange -> updateDuration(action.value)
            CreateRoutineAction.OnToggleEnabled -> toggleEnabled()
            CreateRoutineAction.OnSaveClick -> saveRoutine()
        }
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
        val duration = value
            .filter { it.isDigit() }
            .take(4)
            .toIntOrNull() ?: 0

        state = state.copy(
            durationSec = duration,
            errorMessage = null,
        )
    }

    private fun toggleEnabled() {
        state = state.copy(
            enabled = !state.enabled,
            errorMessage = null,
        )
    }

    private fun saveRoutine() {
        val time = state.time.trim()

        when {
            time.isBlank() -> {
                state = state.copy(errorMessage = "Informe o horario da rotina.")
                return
            }
            state.selectedDays.isEmpty() -> {
                state = state.copy(errorMessage = "Selecione pelo menos um dia.")
                return
            }
            state.durationSec <= 0 -> {
                state = state.copy(errorMessage = "Informe uma duracao maior que zero.")
                return
            }
        }

        state = state.copy(
            name = state.name.trim(),
            time = time,
            isSaving = true,
            errorMessage = null,
        )

        state = state.copy(isSaving = false)
        onCreated()
    }
}
