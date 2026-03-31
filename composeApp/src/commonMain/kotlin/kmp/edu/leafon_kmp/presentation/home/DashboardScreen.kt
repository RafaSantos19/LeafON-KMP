package kmp.edu.leafon_kmp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.home.components.AlertListCard
import kmp.edu.leafon_kmp.presentation.home.components.AutomationSummaryCard
import kmp.edu.leafon_kmp.presentation.home.components.ChartRange
import kmp.edu.leafon_kmp.presentation.home.components.HumidityChartCard
import kmp.edu.leafon_kmp.presentation.home.components.IrrigationListCard
import kmp.edu.leafon_kmp.presentation.home.components.MetricCard
import kmp.edu.leafon_kmp.presentation.home.components.PlantHeroCard
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.profile.ProfileScreen
import kmp.edu.leafon_kmp.presentation.profile.ProfileViewModel

@Composable
fun DashboardScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary)
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactDashboardLayout(
                state = state,
                onAction = onAction,
                onLoggedOut = onLoggedOut,
            )
        } else {
            ExpandedDashboardLayout(
                state = state,
                onAction = onAction,
                onLoggedOut = onLoggedOut,
            )
        }
    }
}

@Composable
private fun ExpandedDashboardLayout(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onLoggedOut: () -> Unit,
) {
    val topBarState = AppTopBarState(
        title = "Dashboard",
        subject = state.dashboard.plantStatus.name,
        subjectOnline = state.dashboard.plantStatus.deviceOnline,
        lastUpdateLabel = "Last update: ${state.dashboard.plantStatus.lastUpdate}",
    )

    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = state.selectedDestination,
            onHomeClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.HOME))
            },
            onHistoryClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.HISTORY))
            },
            onPlantAndPotClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PLANT_AND_POT))
            },
            onAlertsClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.ALERTS))
            },
            onProfileClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PROFILE))
            },
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LeafOnColors.BorderDefault),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            if (state.selectedDestination != SidebarDestination.PROFILE) {
                AppTopBar(
                    state = topBarState,
                    onNotificationsClick = { onAction(HomeAction.OnNotificationsClick) },
                    onProfileClick = { onAction(HomeAction.OnProfileClick) },
                )
            }

            DashboardContent(
                state = state,
                onAction = onAction,
                onLoggedOut = onLoggedOut,
                isCompact = false,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactDashboardLayout(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onLoggedOut: () -> Unit,
) {
    val topBarState = AppTopBarState(
        title = "Dashboard",
        subject = state.dashboard.plantStatus.name,
        subjectOnline = state.dashboard.plantStatus.deviceOnline,
        lastUpdateLabel = "Last update: ${state.dashboard.plantStatus.lastUpdate}",
    )

    Column(modifier = Modifier.fillMaxSize()) {
        if (state.selectedDestination != SidebarDestination.PROFILE) {
            AppTopBar(
                state = topBarState,
                onNotificationsClick = { onAction(HomeAction.OnNotificationsClick) },
                onProfileClick = { onAction(HomeAction.OnProfileClick) },
                compact = true,
            )
        }

        AppSidebar(
            selectedDestination = state.selectedDestination,
            onHomeClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.HOME))
            },
            onHistoryClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.HISTORY))
            },
            onPlantAndPotClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PLANT_AND_POT))
            },
            onAlertsClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.ALERTS))
            },
            onProfileClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PROFILE))
            },
            compact = true,
        )

        DashboardContent(
            state = state,
            onAction = onAction,
            onLoggedOut = onLoggedOut,
            isCompact = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DashboardContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onLoggedOut: () -> Unit,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    val profileViewModel = remember { ProfileViewModel() }

    if (state.selectedDestination == SidebarDestination.PROFILE) {
        ProfileScreen(
            viewModel = profileViewModel,
            onLoggedOut = onLoggedOut,
            modifier = modifier,
        )
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            if (isCompact) {
                PlantHeroCard(
                    plantStatus = state.dashboard.plantStatus,
                    onWaterNowClick = { onAction(HomeAction.OnWaterNowClick) },
                )
                AutomationSummaryCard(summary = state.dashboard.automationSummary)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    PlantHeroCard(
                        plantStatus = state.dashboard.plantStatus,
                        onWaterNowClick = { onAction(HomeAction.OnWaterNowClick) },
                        modifier = Modifier.weight(0.62f),
                    )
                    AutomationSummaryCard(
                        summary = state.dashboard.automationSummary,
                        modifier = Modifier.weight(0.38f),
                    )
                }
            }

            MetricGrid(
                state = state,
                isCompact = isCompact,
            )

            HumidityChartCard(
                selectedRange = state.selectedRange,
                onRangeSelected = { range ->
                    onAction(HomeAction.OnRangeSelected(range))
                },
            )

            if (isCompact) {
                IrrigationListCard(irrigations = state.dashboard.recentIrrigations)
                AlertListCard(alerts = state.dashboard.recentAlerts)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    IrrigationListCard(
                        irrigations = state.dashboard.recentIrrigations,
                        modifier = Modifier.weight(1f),
                    )
                    AlertListCard(
                        alerts = state.dashboard.recentAlerts,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        when {
            state.dashboard.isLoading -> DashboardLoadingState()
            state.dashboard.errorMessage != null -> DashboardErrorState(
                message = state.dashboard.errorMessage,
                onRetryClick = { onAction(HomeAction.OnRetryClick) }
            )
        }
    }
}

@Composable
private fun MetricGrid(
    state: HomeState,
    isCompact: Boolean,
) {
    if (isCompact) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            state.dashboard.metrics.forEach { metric ->
                MetricCard(
                    metric = metric,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            state.dashboard.metrics.forEach { metric ->
                MetricCard(
                    metric = metric,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun DashboardLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Carregando dashboard...",
                color = LeafOnColors.TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun DashboardErrorState(
    message: String,
    onRetryClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary.copy(alpha = 0.94f))
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Nao foi possivel carregar a home.",
                style = MaterialTheme.typography.titleMedium,
                color = LeafOnColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = LeafOnColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = onRetryClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LeafOnColors.GreenPrimary,
                    contentColor = LeafOnColors.TextOnDark,
                )
            ) {
                Text("Tentar novamente")
            }
        }
    }
}

