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
import kmp.edu.leafon_kmp.presentation.pots.PotListAction
import kmp.edu.leafon_kmp.presentation.pots.PotListContent
import kmp.edu.leafon_kmp.presentation.pots.PotListViewModel

@Composable
fun DashboardScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit = {},
    onPotsClick: (() -> Unit)? = null,
    onAlertsClick: (() -> Unit)? = null,
    onProfileClick: (() -> Unit)? = null,
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
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onLoggedOut = onLoggedOut,
            )
        } else {
            ExpandedDashboardLayout(
                state = state,
                onAction = onAction,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onLoggedOut = onLoggedOut,
            )
        }
    }
}

@Composable
private fun ExpandedDashboardLayout(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onPotsClick: (() -> Unit)?,
    onAlertsClick: (() -> Unit)?,
    onProfileClick: (() -> Unit)?,
    onLoggedOut: () -> Unit,
) {
    val topBarState = dashboardTopBarState(state)

    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = state.selectedDestination,
            onHomeClick = {
                onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.HOME))
            },
            onPlantAndPotClick = {
                if (onPotsClick != null) {
                    onPotsClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PLANT_AND_POT))
                }
            },
            onAlertsClick = {
                if (onAlertsClick != null) {
                    onAlertsClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.ALERTS))
                }
            },
            onProfileClick = {
                if (onProfileClick != null) {
                    onProfileClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PROFILE))
                }
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
    onPotsClick: (() -> Unit)?,
    onAlertsClick: (() -> Unit)?,
    onProfileClick: (() -> Unit)?,
    onLoggedOut: () -> Unit,
) {
    val topBarState = dashboardTopBarState(state)

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
            onPlantAndPotClick = {
                if (onPotsClick != null) {
                    onPotsClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PLANT_AND_POT))
                }
            },
            onAlertsClick = {
                if (onAlertsClick != null) {
                    onAlertsClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.ALERTS))
                }
            },
            onProfileClick = {
                if (onProfileClick != null) {
                    onProfileClick()
                } else {
                    onAction(HomeAction.OnSidebarDestinationSelected(SidebarDestination.PROFILE))
                }
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
    val potListViewModel = remember { PotListViewModel() }

    if (state.selectedDestination == SidebarDestination.PROFILE) {
        ProfileScreen(
            viewModel = profileViewModel,
            onLoggedOut = onLoggedOut,
            modifier = modifier,
        )
        return
    }

    if (state.selectedDestination == SidebarDestination.PLANT_AND_POT) {
        PotListContent(
            state = potListViewModel.state,
            onPotClick = { id ->
                potListViewModel.onAction(PotListAction.OnPotClick(id))
            },
            onEditPotClick = { id ->
                potListViewModel.onAction(PotListAction.OnEditPotClick(id))
            },
            onDeletePotClick = { id ->
                potListViewModel.onAction(PotListAction.OnDeletePotClick(id))
            },
            onAddPotClick = {
                potListViewModel.onAction(PotListAction.OnAddPotClick)
            },
            onRefreshClick = {
                potListViewModel.onAction(PotListAction.OnRefresh)
            },
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

private fun dashboardTopBarState(state: HomeState): AppTopBarState {
    val title = when (state.selectedDestination) {
        SidebarDestination.HOME -> "Início"
        SidebarDestination.PLANT_AND_POT -> "Planta & Vaso"
        SidebarDestination.ALERTS -> "Alertas"
        SidebarDestination.PROFILE -> "Perfil"
    }

    return AppTopBarState(
        title = title,
        subject = state.dashboard.plantStatus.name,
        subjectOnline = state.dashboard.plantStatus.deviceOnline,
        lastUpdateLabel = "Última atualização: ${state.dashboard.plantStatus.lastUpdate}",
    )
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
                text = "Carregando página inicial...",
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
                text = "Não foi possível carregar a página inicial.",
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

