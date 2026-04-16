package kmp.edu.leafon_kmp.presentation.pots.detail

sealed interface PotDetailAction {
    data object OnEditClick : PotDetailAction
    data object OnViewRoutinesClick : PotDetailAction
    data object OnViewAlertsClick : PotDetailAction
    data object OnRetryClick : PotDetailAction
}
