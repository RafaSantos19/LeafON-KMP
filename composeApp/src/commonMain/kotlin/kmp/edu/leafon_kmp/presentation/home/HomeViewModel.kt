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
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class HomeViewModel(
    private val smartPotRepository: SmartPotRepository,
    private val telemetryRepository: TelemetryRepository,
    private val alertRepository: AlertRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var dashboardJob: Job? = null
    private var telemetryJob: Job? = null

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
        dashboardJob?.cancel()
        telemetryJob?.cancel()

        dashboardJob = scope.launch {
            state = state.copy(
                isLoading = true,
                isTelemetryLoading = false,
                errorMessage = null,
                telemetryErrorMessage = null,
            )

            try {
                val selectedSmartPotId = supervisorScope {
                    val smartPotsDeferred = async { smartPotRepository.listSmartPots() }
                    val unreadAlertsDeferred = async { alertRepository.listUnreadAlerts() }

                    val smartPots = smartPotsDeferred.await()
                    val resolvedSelectedSmartPotId = resolveSelectedSmartPotId(smartPots)

                    state = state.copy(
                        smartPots = smartPots,
                        selectedSmartPotId = resolvedSelectedSmartPotId,
                        unreadAlertsCount = unreadAlertsDeferred.await().size,
                        latestTelemetry = null,
                        telemetryHistory = emptyList(),
                        isLoading = false,
                        errorMessage = null,
                    )

                    resolvedSelectedSmartPotId
                }

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
        telemetryJob?.cancel()

        telemetryJob = scope.launch {
            state = state.copy(
                isTelemetryLoading = true,
                telemetryErrorMessage = null,
            )

            try {
                supervisorScope {
                    val latestTelemetryDeferred = async {
                        runTelemetryRequest {
                            telemetryRepository.getLatestTelemetry(smartPotId)
                        }
                    }
                    val telemetryHistoryDeferred = async {
                        runTelemetryRequest {
                            telemetryRepository.getTelemetry(
                                smartPotId = smartPotId,
                                limit = MAX_DASHBOARD_TELEMETRY_POINTS,
                            )
                        }
                    }

                    val latestResult = latestTelemetryDeferred.await()
                    val historyResult = telemetryHistoryDeferred.await()
                    val telemetryError = latestResult.exceptionOrNull()
                        ?: historyResult.exceptionOrNull()

                    state = state.copy(
                        latestTelemetry = latestResult.getOrNull(),
                        telemetryHistory = historyResult.getOrDefault(emptyList()),
                        isTelemetryLoading = false,
                        telemetryErrorMessage = telemetryError?.let(HomeErrorMapper::fromThrowable),
                    )
                }
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

    private suspend fun <T> runTelemetryRequest(block: suspend () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (throwable: CancellationException) {
            throw throwable
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }

    private companion object {
        const val MAX_DASHBOARD_TELEMETRY_POINTS = 30
    }
}
