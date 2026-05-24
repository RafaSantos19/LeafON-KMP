package kmp.edu.leafon_kmp.presentation.pots.alerts.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.data.repository.AlertRepository
import kmp.edu.leafon_kmp.presentation.pots.AlertErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.AlertOperation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AlertListViewModel(
    private val potId: String,
    private val alertRepository: AlertRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(AlertListState(potId = potId))
        private set

    init {
        loadAlerts()
    }

    fun onAction(action: AlertListAction) {
        when (action) {
            is AlertListAction.OnUnreadFilterChange -> {
                if (action.showUnreadOnly != state.showUnreadOnly) {
                    state = state.copy(showUnreadOnly = action.showUnreadOnly)
                    loadAlerts()
                }
            }

            is AlertListAction.OnMarkAsReadClick -> markAsRead(action.alertId)
            AlertListAction.OnRetryClick -> loadAlerts()
            AlertListAction.OnDismissMessage -> {
                state = state.copy(errorMessage = null, successMessage = null)
            }
            AlertListAction.OnBackClick -> Unit
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadAlerts() {
        scope.launch {
            state = state.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

            try {
                val alerts = if (state.showUnreadOnly) {
                    alertRepository.listUnreadAlerts()
                } else {
                    alertRepository.listAlerts()
                }.filterVisibleAlerts()

                state = state.copy(
                    alerts = alerts,
                    isLoading = false,
                    errorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoading = false,
                    errorMessage = AlertErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = AlertOperation.LIST,
                    ),
                )
            }
        }
    }

    private fun markAsRead(alertId: String) {
        if (state.isMarkingAsRead) {
            return
        }

        scope.launch {
            state = state.copy(
                isMarkingAsRead = true,
                markingAlertId = alertId,
                errorMessage = null,
                successMessage = null,
            )

            try {
                val updatedAlert = alertRepository.markAsRead(alertId)
                val updatedAlerts = if (state.showUnreadOnly) {
                    state.alerts.filterNot { it.id == alertId }
                } else {
                    state.alerts.map { current ->
                        if (current.id == alertId) updatedAlert else current
                    }
                }

                state = state.copy(
                    alerts = updatedAlerts.filterVisibleAlerts(),
                    isMarkingAsRead = false,
                    markingAlertId = null,
                    successMessage = if (updatedAlert.status == AlertStatus.READ) {
                        "Alerta marcado como lido."
                    } else {
                        "Alerta atualizado com sucesso."
                    },
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isMarkingAsRead = false,
                    markingAlertId = null,
                    errorMessage = AlertErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = AlertOperation.MARK_AS_READ,
                    ),
                )
            }
        }
    }

    private fun List<kmp.edu.leafon_kmp.core.model.Alert>.filterVisibleAlerts(): List<kmp.edu.leafon_kmp.core.model.Alert> {
        val resolvedPotId = potId.trim()
        return asSequence()
            .let { alerts ->
                if (resolvedPotId.isBlank()) {
                    alerts
                } else {
                    alerts.filter { it.smartPotId == resolvedPotId }
                }
            }
            .toList()
    }
}
