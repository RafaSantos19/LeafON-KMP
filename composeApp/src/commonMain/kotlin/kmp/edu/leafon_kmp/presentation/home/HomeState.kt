package kmp.edu.leafon_kmp.presentation.home

import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.home.components.ChartRange
import kmp.edu.leafon_kmp.presentation.home.model.DashboardUiState
import kmp.edu.leafon_kmp.presentation.home.model.dashboardPreviewState

data class HomeState(
    val dashboard: DashboardUiState = DashboardUiState(),
    val selectedDestination: SidebarDestination = SidebarDestination.HOME,
    val selectedRange: ChartRange = ChartRange.H24,
)

fun homePreviewState() = HomeState(
    dashboard = dashboardPreviewState(),
)
