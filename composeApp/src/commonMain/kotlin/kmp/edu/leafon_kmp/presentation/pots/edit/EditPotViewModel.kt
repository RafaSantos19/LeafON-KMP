package kmp.edu.leafon_kmp.presentation.pots.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory
import kmp.edu.leafon_kmp.presentation.pots.SmartPotErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.SmartPotOperation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditPotViewModel(
    potId: String,
    private val smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
    private val onUpdated: () -> Unit = {},
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(EditPotState(potId = potId))
        private set

    init {
        loadPot(potId)
    }

    fun onAction(action: EditPotAction) {
        when (action) {
            is EditPotAction.OnPlantNameChange -> updatePlantName(action.value)
            is EditPotAction.OnHumidityMinChange -> updateHumidityMin(action.value)
            is EditPotAction.OnDeviceIdChange -> updateDeviceId(action.value)
            EditPotAction.OnSaveClick -> saveChanges()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadPot(potId: String) {
        scope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            try {
                val pot = smartPotRepository.getSmartPotById(potId)
                state = state.copy(
                    potId = potId,
                    plantName = pot.plantName,
                    humidityMin = pot.humidityMin.toString(),
                    deviceId = pot.deviceId.orEmpty(),
                    isLoading = false,
                    errorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = SmartPotErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = SmartPotOperation.DETAIL,
                    ),
                )
            }
        }
    }

    private fun updatePlantName(value: String) {
        state = state.copy(
            plantName = value,
            errorMessage = null,
        )
    }

    private fun updateHumidityMin(value: String) {
        state = state.copy(
            humidityMin = value.filter { it.isDigit() },
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
        val plantName = state.plantName.trim()
        val humidityMinInput = state.humidityMin.trim()
        val humidityMin = humidityMinInput.toIntOrNull()

        when {
            plantName.isBlank() -> {
                state = state.copy(errorMessage = "Informe o nome da planta.")
                return
            }
            humidityMin == null -> {
                state = state.copy(errorMessage = "Informe a umidade minima entre 0 e 100.")
                return
            }
            humidityMin !in 0..100 -> {
                state = state.copy(errorMessage = "A umidade minima deve estar entre 0 e 100.")
                return
            }
        }

        state = state.copy(
            plantName = plantName,
            humidityMin = humidityMinInput,
            deviceId = state.deviceId.trim(),
            isSaving = true,
            errorMessage = null,
        )

        scope.launch {
            try {
                smartPotRepository.updateSmartPot(
                    id = state.potId,
                    plantName = plantName,
                    humidityMin = humidityMin,
                    deviceId = state.deviceId.takeIf { it.isNotBlank() },
                )
                state = state.copy(isSaving = false, errorMessage = null)
                onUpdated()
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isSaving = false,
                    errorMessage = SmartPotErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = SmartPotOperation.UPDATE,
                    ),
                )
            }
        }
    }
}
