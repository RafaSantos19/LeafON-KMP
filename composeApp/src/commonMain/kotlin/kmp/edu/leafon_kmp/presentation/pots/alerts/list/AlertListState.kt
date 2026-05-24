package kmp.edu.leafon_kmp.presentation.pots.alerts.list

import kmp.edu.leafon_kmp.core.model.Alert

data class AlertListState(
    val potId: String,
    val alerts: List<Alert> = emptyList(),
    val showUnreadOnly: Boolean = false,
    val isLoading: Boolean = true,
    val isMarkingAsRead: Boolean = false,
    val markingAlertId: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)
