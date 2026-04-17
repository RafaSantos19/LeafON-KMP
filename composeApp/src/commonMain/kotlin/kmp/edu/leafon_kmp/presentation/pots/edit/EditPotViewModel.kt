package kmp.edu.leafon_kmp.presentation.pots.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria

class EditPotViewModel(
    potId: String,
    private val repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
    private val onUpdated: () -> Unit = {},
) {

    var state by mutableStateOf(EditPotState(potId = potId))
        private set

    init {
        loadPot(potId)
    }

    fun onAction(action: EditPotAction) {
        when (action) {
            is EditPotAction.OnNameChange -> updateName(action.value)
            is EditPotAction.OnPlantNameChange -> updatePlantName(action.value)
            is EditPotAction.OnDeviceIdChange -> updateDeviceId(action.value)
            EditPotAction.OnSaveClick -> saveChanges()
        }
    }

    private fun loadPot(potId: String) {
        val pot = repositorio.getPotById(potId)

        state = if (pot == null) {
            state.copy(
                isLoading = false,
                errorMessage = "Pot nao encontrado.",
            )
        } else {
            state.copy(
                potId = potId,
                name = pot.name,
                plantName = pot.plantType,
                deviceId = pot.deviceId,
                isLoading = false,
                errorMessage = null,
            )
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

    private fun saveChanges() {
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

        val currentPot = repositorio.getPotById(state.potId)

        if (currentPot == null) {
            state = state.copy(
                isSaving = false,
                errorMessage = "Pot nao encontrado.",
            )
            return
        }

        repositorio.putPot(
            currentPot.copy(
                name = potName,
                plantType = plantName,
                deviceId = state.deviceId,
                lastUpdateLabel = "Agora",
            )
        )

        state = state.copy(isSaving = false)
        onUpdated()
    }
}
