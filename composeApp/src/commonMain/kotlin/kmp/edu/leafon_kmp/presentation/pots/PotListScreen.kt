package kmp.edu.leafon_kmp.presentation.pots

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.core.time.ReadableTimestampFormatter
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination

@Composable
fun PotListScreen(
    viewModel: PotListViewModel,
    onNavigateToPotDetail: (String) -> Unit = {},
    onNavigateToEditPot: (String) -> Unit = {},
    onAddPotClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var pendingDeletePotId by remember { mutableStateOf<String?>(null) }

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
                    pendingDeletePotId = id
                },
                onAddPotClick = {
                    viewModel.onAction(PotListAction.OnAddPotClick)
                    onAddPotClick()
                },
                onRefreshClick = {
                    viewModel.onAction(PotListAction.OnRefresh)
                },
                onHomeClick = onHomeClick,
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
                    pendingDeletePotId = id
                },
                onAddPotClick = {
                    viewModel.onAction(PotListAction.OnAddPotClick)
                    onAddPotClick()
                },
                onRefreshClick = {
                    viewModel.onAction(PotListAction.OnRefresh)
                },
                onHomeClick = onHomeClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        }

        val potToDelete = viewModel.state.pots.firstOrNull { it.id == pendingDeletePotId }

        if (potToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    pendingDeletePotId = null
                },
                title = {
                    Text("Excluir vaso")
                },
                text = {
                    Text("Deseja excluir ${potToDelete.plantName}? Esta acao nao pode ser desfeita.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onAction(PotListAction.OnDeletePotClick(potToDelete.id))
                            pendingDeletePotId = null
                        },
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            pendingDeletePotId = null
                        },
                    ) {
                        Text("Cancelar")
                    }
                },
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
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
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
    val lastUpdate = ReadableTimestampFormatter.formatOrFallback(
        state.pots.firstOrNull()?.updatedAt
            ?: state.pots.firstOrNull()?.createdAt
    )
        ?: "sem atualizacao"

    return AppTopBarState(
        title = "Planta & Vaso",
        subject = "$totalPots vasos cadastrados",
        subjectOnline = totalPots > 0,
        lastUpdateLabel = "Ultima atualizacao: $lastUpdate",
    )
}
