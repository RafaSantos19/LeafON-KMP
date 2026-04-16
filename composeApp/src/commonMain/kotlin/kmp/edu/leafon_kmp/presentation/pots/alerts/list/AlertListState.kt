package kmp.edu.leafon_kmp.presentation.pots.alerts.list

import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertUi

data class AlertListState(
    val potId: String,
    val alerts: List<AlertUi> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
