package kmp.edu.leafon_kmp.presentation.pots.alerts.list

sealed interface AlertListAction {
    data class OnUnreadFilterChange(val showUnreadOnly: Boolean) : AlertListAction
    data class OnMarkAsReadClick(val alertId: String) : AlertListAction
    data object OnRetryClick : AlertListAction
    data object OnDismissMessage : AlertListAction
    data object OnBackClick : AlertListAction
}
