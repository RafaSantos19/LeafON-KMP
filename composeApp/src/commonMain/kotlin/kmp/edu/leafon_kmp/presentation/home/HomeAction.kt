package kmp.edu.leafon_kmp.presentation.home

import kmp.edu.leafon_kmp.presentation.home.components.ChartRange
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination

sealed class HomeAction {
    data object OnWaterNowClick : HomeAction()
    data object OnNotificationsClick : HomeAction()
    data object OnProfileClick : HomeAction()
    data object OnRetryClick : HomeAction()
    data class OnRangeSelected(val range: ChartRange) : HomeAction()
    data class OnSidebarDestinationSelected(val destination: SidebarDestination) : HomeAction()
}
