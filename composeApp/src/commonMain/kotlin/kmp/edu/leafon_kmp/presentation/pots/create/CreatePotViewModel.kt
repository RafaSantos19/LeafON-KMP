package kmp.edu.leafon_kmp.presentation.pots.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CreatePotViewModel(
    private val onCreated: () -> Unit = {},
) {

    var state by mutableStateOf(CreatePotState())
        private set

    fun onAction(action: CreatePotAction) {
        when (action) {
            is CreatePotAction.OnNameChange -> updateName(action.value)
            is CreatePotAction.OnPlantNameChange -> updatePlantName(action.value)
            is CreatePotAction.OnDeviceIdChange -> updateDeviceId(action.value)
            CreatePotAction.OnSaveClick -> savePot()
        }
    }

    private fun updateName(value: String) {
        state = state.copy(
            name = value,
            errorMessage = null,
        )
    }

    private fun updatePlantName(value: String) {
        state = state.copy(
            plantName = value,
            errorMessage = null,
        )
    }

    private fun updateDeviceId(value: String) {
        state = state.copy(
            deviceId = value,
            errorMessage = null,
        )
    }

    private fun savePot() {
        val potName = state.name.trim()
        val plantName = state.plantName.trim()

        when {
            potName.isBlank() -> {
                state = state.copy(errorMessage = "Informe o nome do pot.")
                return
            }
            plantName.isBlank() -> {
                state = state.copy(errorMessage = "Informe o nome da planta.")
                return
            }
        }

        state = state.copy(
            name = potName,
            plantName = plantName,
            deviceId = state.deviceId.trim(),
            isSaving = true,
            errorMessage = null,
        )

        state = state.copy(isSaving = false)
        onCreated()
    }
}
