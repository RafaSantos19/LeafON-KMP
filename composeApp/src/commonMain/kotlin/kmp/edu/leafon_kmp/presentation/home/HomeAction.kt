package kmp.edu.leafon_kmp.presentation.home

import kmp.edu.leafon_kmp.presentation.home.components.ChartRange

sealed interface HomeAction {
    data object OnRetryClick : HomeAction
    data object OnRefreshClick : HomeAction
    data class OnRangeSelected(val range: ChartRange) : HomeAction
    data class OnSmartPotSelected(val smartPotId: String) : HomeAction
}
