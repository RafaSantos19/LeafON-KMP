package kmp.edu.leafon_kmp.presentation.pots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

@Composable
fun PotListScreen(
    viewModel: PotListViewModel,
    onNavigateToPotDetail: (String) -> Unit = {},
    onNavigateToEditPot: (String) -> Unit = {},
    onDeletePot: (String) -> Unit = {},
    onAddPotClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactPotListLayout(
                state = viewModel.state,
                onPotClick = { id ->
                    viewModel.onAction(PotListAction.OnPotClick(id))
                    onNavigateToPotDetail(id)
                },
                onEditPotClick = { id ->
                    viewModel.onAction(PotListAction.OnEditPotClick(id))
                    onNavigateToEditPot(id)
                },
                onDeletePotClick = { id ->
                    viewModel.onAction(PotListAction.OnDeletePotClick(id))
                    onDeletePot(id)
                },
                onAddPotClick = {
                    viewModel.onAction(PotListAction.OnAddPotClick)
                    onAddPotClick()
                },
                onRefreshClick = {
                    viewModel.onAction(PotListAction.OnRefresh)
                },
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        } else {
            ExpandedPotListLayout(
                state = viewModel.state,
                onPotClick = { id ->
                    viewModel.onAction(PotListAction.OnPotClick(id))
                    onNavigateToPotDetail(id)
                },
                onEditPotClick = { id ->
                    viewModel.onAction(PotListAction.OnEditPotClick(id))
                    onNavigateToEditPot(id)
                },
                onDeletePotClick = { id ->
                    viewModel.onAction(PotListAction.OnDeletePotClick(id))
                    onDeletePot(id)
                },
                onAddPotClick = {
                    viewModel.onAction(PotListAction.OnAddPotClick)
                    onAddPotClick()
                },
                onRefreshClick = {
                    viewModel.onAction(PotListAction.OnRefresh)
                },
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        }
    }
}

@Composable
private fun ExpandedPotListLayout(
    state: PotListState,
    onPotClick: (String) -> Unit,
    onEditPotClick: (String) -> Unit,
    onDeletePotClick: (String) -> Unit,
    onAddPotClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onHistoryClick = onHistoryClick,
            onPlantAndPotClick = {},
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LeafOnColors.BorderDefault),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                state = potListTopBarState(state),
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )

            PotListContent(
                state = state,
                onPotClick = onPotClick,
                onEditPotClick = onEditPotClick,
                onDeletePotClick = onDeletePotClick,
                onAddPotClick = onAddPotClick,
                onRefreshClick = onRefreshClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactPotListLayout(
    state: PotListState,
    onPotClick: (String) -> Unit,
    onEditPotClick: (String) -> Unit,
    onDeletePotClick: (String) -> Unit,
    onAddPotClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = potListTopBarState(state),
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onHistoryClick = onHistoryClick,
            onPlantAndPotClick = {},
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        PotListContent(
            state = state,
            onPotClick = onPotClick,
            onEditPotClick = onEditPotClick,
            onDeletePotClick = onDeletePotClick,
            onAddPotClick = onAddPotClick,
            onRefreshClick = onRefreshClick,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun potListTopBarState(state: PotListState): AppTopBarState {
    val totalPots = state.pots.size
    val onlinePots = state.pots.count { it.status == PotStatus.ONLINE }
    val lastUpdate = state.pots.firstOrNull()?.lastUpdateLabel ?: "sem atualizacao"

    return AppTopBarState(
        title = "Planta & Vaso",
        subject = "$onlinePots/$totalPots vasos online",
        subjectOnline = onlinePots > 0,
        lastUpdateLabel = "Ultima atualizacao: $lastUpdate",
    )
}
