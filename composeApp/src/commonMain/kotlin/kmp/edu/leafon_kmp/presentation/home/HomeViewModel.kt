package kmp.edu.leafon_kmp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.home.model.IrrigationUi

class HomeViewModel {

    var state by mutableStateOf(HomeState())
        private set

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnNotificationsClick -> Unit
            HomeAction.OnProfileClick -> state = state.copy(
                selectedDestination = SidebarDestination.PROFILE
            )
            HomeAction.OnRetryClick -> retry()
            HomeAction.OnWaterNowClick -> triggerManualWatering()
            is HomeAction.OnRangeSelected -> state = state.copy(selectedRange = action.range)
            is HomeAction.OnSidebarDestinationSelected -> state = state.copy(
                selectedDestination = action.destination
            )
        }
    }

    private fun retry() {
        state = state.copy(
            dashboard = state.dashboard.copy(
                isLoading = false,
                errorMessage = null,
            ),
        )
    }

    private fun triggerManualWatering() {
        val current = state.dashboard

        if (current.plantStatus.name.isBlank()) return

        state = state.copy(
            dashboard = current.copy(
                recentIrrigations = listOf(
                    current.recentIrrigations.firstOrNull()?.copy(
                        timestamp = "Agora mesmo",
                        type = "Manual",
                    ) ?: IrrigationUi(
                        timestamp = "Agora mesmo",
                        type = "Manual",
                    )
                ) + current.recentIrrigations,
                plantStatus = current.plantStatus.copy(
                    lastUpdate = "Agora mesmo",
                ),
            ),
        )
    }
}
