package kmp.edu.leafon_kmp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.data.repository.AlertRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.TelemetryRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val smartPotRepository: SmartPotRepository,
    private val telemetryRepository: TelemetryRepository,
    private val alertRepository: AlertRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(HomeState(isLoading = true))
        private set

    init {
        loadDashboard()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRetryClick,
            HomeAction.OnRefreshClick,
            -> loadDashboard()

            is HomeAction.OnRangeSelected -> {
                state = state.copy(selectedRange = action.range)
            }

            is HomeAction.OnSmartPotSelected -> {
                if (action.smartPotId != state.selectedSmartPotId) {
                    state = state.copy(
                        selectedSmartPotId = action.smartPotId,
                        latestTelemetry = null,
                        telemetryHistory = emptyList(),
                        telemetryErrorMessage = null,
                    )
                    loadTelemetryForSelectedPot(action.smartPotId)
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadDashboard() {
        scope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
                telemetryErrorMessage = null,
            )

            try {
                val smartPotsDeferred = async { smartPotRepository.listSmartPots() }
                val unreadAlertsDeferred = async { alertRepository.listUnreadAlerts() }

                val smartPots = smartPotsDeferred.await()
                val selectedSmartPotId = resolveSelectedSmartPotId(smartPots)

                state = state.copy(
                    smartPots = smartPots,
                    selectedSmartPotId = selectedSmartPotId,
                    unreadAlertsCount = unreadAlertsDeferred.await().size,
                    latestTelemetry = null,
                    telemetryHistory = emptyList(),
                    isLoading = false,
                    errorMessage = null,
                )

                if (selectedSmartPotId != null) {
                    loadTelemetryForSelectedPot(selectedSmartPotId)
                }
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = HomeErrorMapper.fromThrowable(throwable),
                )
            }
        }
    }

    private fun loadTelemetryForSelectedPot(smartPotId: String) {
        scope.launch {
            state = state.copy(
                isTelemetryLoading = true,
                telemetryErrorMessage = null,
            )

            try {
                val latestTelemetryDeferred = async {
                    telemetryRepository.getLatestTelemetry(smartPotId)
                }
                val telemetryHistoryDeferred = async {
                    telemetryRepository.getTelemetry(smartPotId)
                }

                state = state.copy(
                    latestTelemetry = latestTelemetryDeferred.await(),
                    telemetryHistory = telemetryHistoryDeferred.await(),
                    isTelemetryLoading = false,
                    telemetryErrorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    latestTelemetry = null,
                    telemetryHistory = emptyList(),
                    isTelemetryLoading = false,
                    telemetryErrorMessage = HomeErrorMapper.fromThrowable(throwable),
                )
            }
        }
    }

    private fun resolveSelectedSmartPotId(smartPots: List<SmartPot>): String? {
        val currentSelectedSmartPotId = state.selectedSmartPotId

        return when {
            smartPots.isEmpty() -> null
            currentSelectedSmartPotId != null &&
                smartPots.any { it.id == currentSelectedSmartPotId } -> {
                currentSelectedSmartPotId
            }

            else -> smartPots.first().id
        }
    }
}
