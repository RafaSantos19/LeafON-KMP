package kmp.edu.leafon_kmp.presentation.pots.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi

class CreatePotViewModel(
    private val repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
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

        repositorio.postPot(
            PotUi(
                id = generatePotId(),
                name = potName,
                plantType = plantName,
                status = PotStatus.OFFLINE,
                humidityPercent = null,
                temperatureCelsius = null,
                lastUpdateLabel = "Agora",
                deviceId = state.deviceId,
            )
        )

        state = state.copy(isSaving = false)
        onCreated()
    }

    private fun generatePotId(): String {
        val lastNumericId = repositorio
            .getPots()
            .mapNotNull { it.id.toLongOrNull() }
            .maxOrNull() ?: 0L

        return (lastNumericId + 1L).toString()
    }
}
