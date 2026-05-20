package kmp.edu.leafon_kmp.presentation.pots.alerts.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AlertListViewModel(
    private val potId: String,
) {

    var state by mutableStateOf(AlertListState(potId = potId))
        private set

    init {
        loadAlerts()
    }

    fun onAction(action: AlertListAction) {
        when (action) {
            AlertListAction.OnRetryClick -> loadAlerts()
            AlertListAction.OnBackClick -> Unit
        }
    }

    private fun loadAlerts() {
        state = state.copy(
            alerts = emptyList(),
            isLoading = false,
            errorMessage = null,
        )
    }
}
