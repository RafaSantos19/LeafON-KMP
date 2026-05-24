package kmp.edu.leafon_kmp.presentation.pots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PotListViewModel(
    private val smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(PotListState())
        private set

    init {
        refresh()
    }

    fun onAction(action: PotListAction) {
        when (action) {
            is PotListAction.OnPotClick -> Unit
            is PotListAction.OnEditPotClick -> Unit
            is PotListAction.OnDeletePotClick -> deletePot(action.id)
            is PotListAction.OnAddPotClick -> Unit
            is PotListAction.OnRefresh -> refresh()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun refresh() {
        scope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            try {
                state = state.copy(
                    pots = smartPotRepository.listSmartPots(),
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
                        operation = SmartPotOperation.LIST,
                    ),
                )
            }
        }
    }

    private fun deletePot(id: String) {
        scope.launch {
            state = state.copy(isLoading = true, errorMessage = null)

            try {
                smartPotRepository.deleteSmartPot(id)
                state = state.copy(
                    pots = smartPotRepository.listSmartPots(),
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
                        operation = SmartPotOperation.DELETE,
                    ),
                )
            }
        }
    }
}
