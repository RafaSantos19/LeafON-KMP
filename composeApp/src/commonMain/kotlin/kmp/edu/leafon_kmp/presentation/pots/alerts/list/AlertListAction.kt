package kmp.edu.leafon_kmp.presentation.pots.alerts.list

sealed interface AlertListAction {
    data object OnRetryClick : AlertListAction
    data object OnBackClick : AlertListAction
}
