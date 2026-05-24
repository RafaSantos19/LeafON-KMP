package kmp.edu.leafon_kmp.presentation.pots.detail

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

class PotDetailViewModel(
    private val potId: String,
    private val smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(PotDetailState(potId = potId))
        private set

    init {
        loadPot()
    }

    fun onAction(action: PotDetailAction) {
        when (action) {
            PotDetailAction.OnEditClick -> Unit
            PotDetailAction.OnViewRoutinesClick -> Unit
            PotDetailAction.OnViewAlertsClick -> Unit
            PotDetailAction.OnRetryClick -> loadPot()
        }
    }

    fun deletePot(onDeleted: () -> Unit) {
        scope.launch {
            state = state.copy(isDeleting = true, errorMessage = null)

            try {
                smartPotRepository.deleteSmartPot(potId)
                state = state.copy(isDeleting = false, errorMessage = null)
                onDeleted()
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isDeleting = false,
                    errorMessage = SmartPotErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = SmartPotOperation.DELETE,
                    ),
                )
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadPot() {
        scope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            try {
                val pot = smartPotRepository.getSmartPotById(potId)
                state = state.copy(
                    potId = pot.id,
                    plantName = pot.plantName,
                    humidityMin = pot.humidityMin,
                    deviceId = pot.deviceId,
                    createdAt = pot.createdAt,
                    updatedAt = pot.updatedAt,
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
}
