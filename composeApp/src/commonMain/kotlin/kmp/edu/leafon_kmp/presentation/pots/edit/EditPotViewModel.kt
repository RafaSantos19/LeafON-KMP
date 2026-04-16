package kmp.edu.leafon_kmp.presentation.pots.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class EditPotViewModel(
    potId: String,
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
        val pot = getFakePotById(potId)

        state = if (pot == null) {
            state.copy(
                isLoading = false,
                errorMessage = "Pot nao encontrado.",
            )
        } else {
            state.copy(
                potId = potId,
                name = pot.name,
                plantName = pot.plantName,
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

        state = state.copy(isSaving = false)
        onUpdated()
    }
}

private data class PotFormData(
    val id: String,
    val name: String,
    val plantName: String,
    val deviceId: String,
)

private fun getFakePotById(id: String): PotFormData? {
    return fakePotForms().firstOrNull { it.id == id }
}

private fun fakePotForms() = listOf(
    PotFormData(
        id = "1",
        name = "Aloe Vera",
        plantName = "Aloe Vera",
        deviceId = "LEAF-001",
    ),
    PotFormData(
        id = "2",
        name = "Monstera Deliciosa",
        plantName = "Monstera",
        deviceId = "LEAF-002",
    ),
    PotFormData(
        id = "3",
        name = "Cacto Sao Jorge",
        plantName = "Cacto Sao Jorge",
        deviceId = "LEAF-003",
    ),
    PotFormData(
        id = "4",
        name = "Samambaia",
        plantName = "Samambaia",
        deviceId = "",
    ),
    PotFormData(
        id = "5",
        name = "Orquidea Phalaenopsis",
        plantName = "Orquidea",
        deviceId = "LEAF-005",
    ),
)
