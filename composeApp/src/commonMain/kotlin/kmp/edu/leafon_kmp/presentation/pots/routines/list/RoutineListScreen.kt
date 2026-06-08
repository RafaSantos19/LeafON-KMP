package kmp.edu.leafon_kmp.presentation.pots.routines.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.data.repository.RoutineRepository
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.routines.components.RoutineCard

@Composable
fun RoutineListScreen(
    potId: String,
    routineRepository: RoutineRepository,
    onBackClick: () -> Unit,
    onCreateRoutineClick: (String) -> Unit,
    onEditRoutineClick: (String, String) -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var routinePendingDeleteId by remember { mutableStateOf<String?>(null) }
    val viewModel = remember(potId, routineRepository) {
        RoutineListViewModel(
            potId = potId,
            routineRepository = routineRepository,
        )
    }

    DisposableEffect(viewModel) {
        onDispose { viewModel.onCleared() }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactRoutineListLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onCreateRoutineClick = onCreateRoutineClick,
                onEditRoutineClick = onEditRoutineClick,
                onDeleteRequest = { routineId ->
                    routinePendingDeleteId = routineId
                },
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        } else {
            ExpandedRoutineListLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onCreateRoutineClick = onCreateRoutineClick,
                onEditRoutineClick = onEditRoutineClick,
                onDeleteRequest = { routineId ->
                    routinePendingDeleteId = routineId
                },
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        }

        if (routinePendingDeleteId != null) {
            AlertDialog(
                onDismissRequest = {
                    routinePendingDeleteId = null
                },
                title = {
                    Text("Excluir rotina")
                },
                text = {
                    Text("Deseja excluir esta rotina? Esta acao nao pode ser desfeita.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val routineId = routinePendingDeleteId
                            routinePendingDeleteId = null
                            if (routineId != null) {
                                viewModel.onAction(RoutineListAction.OnDeleteRoutine(routineId))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LeafOnColors.Error,
                            contentColor = LeafOnColors.TextOnDark,
                        ),
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            routinePendingDeleteId = null
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
private fun ExpandedRoutineListLayout(
    state: RoutineListState,
    onAction: (RoutineListAction) -> Unit,
    onBackClick: () -> Unit,
    onCreateRoutineClick: (String) -> Unit,
    onEditRoutineClick: (String, String) -> Unit,
    onDeleteRequest: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
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
                state = routineListTopBarState(state),
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )

            RoutineListContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                onCreateRoutineClick = onCreateRoutineClick,
                onEditRoutineClick = onEditRoutineClick,
                onDeleteRequest = onDeleteRequest,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactRoutineListLayout(
    state: RoutineListState,
    onAction: (RoutineListAction) -> Unit,
    onBackClick: () -> Unit,
    onCreateRoutineClick: (String) -> Unit,
    onEditRoutineClick: (String, String) -> Unit,
    onDeleteRequest: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = routineListTopBarState(state),
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        RoutineListContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            onCreateRoutineClick = onCreateRoutineClick,
            onEditRoutineClick = onEditRoutineClick,
            onDeleteRequest = onDeleteRequest,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun RoutineListContent(
    state: RoutineListState,
    onAction: (RoutineListAction) -> Unit,
    onBackClick: () -> Unit,
    onCreateRoutineClick: (String) -> Unit,
    onEditRoutineClick: (String, String) -> Unit,
    onDeleteRequest: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isNarrow = maxWidth < 420.dp
        val horizontalPadding = if (isNarrow) 16.dp else 28.dp
        val verticalPadding = if (isNarrow) 18.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
            verticalArrangement = Arrangement.spacedBy(if (isNarrow) 18.dp else 22.dp),
        ) {
        RoutineListHeader(
            compact = isNarrow,
            onBackClick = onBackClick,
            onCreateClick = {
                onAction(RoutineListAction.OnCreateRoutineClick)
                onCreateRoutineClick(state.potId)
            },
        )

        state.successMessage?.let { message ->
            RoutineListFeedback(
                message = message,
                containerColor = LeafOnColors.Success.copy(alpha = 0.12f),
                contentColor = LeafOnColors.Success,
            )
        }

        state.errorMessage?.let { message ->
            if (!state.isLoading) {
                RoutineListFeedback(
                    message = message,
                    containerColor = LeafOnColors.Error.copy(alpha = 0.12f),
                    contentColor = LeafOnColors.Error,
                )
            }
        }

        when {
            state.isLoading -> RoutineListLoadingState()
            state.errorMessage != null && state.routines.isEmpty() -> RoutineListErrorState(
                message = state.errorMessage.orEmpty(),
                onRetryClick = { onAction(RoutineListAction.OnRetryClick) },
            )
            state.routines.isEmpty() -> RoutineListEmptyState(
                onCreateClick = {
                    onAction(RoutineListAction.OnCreateRoutineClick)
                    onCreateRoutineClick(state.potId)
                },
            )
            else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.routines.forEach { routine ->
                    RoutineCard(
                        routine = routine,
                        isBusy = state.busyRoutineId == routine.id,
                        onEditClick = {
                            onEditRoutineClick(state.potId, routine.id)
                        },
                        onToggleActiveClick = {
                            onAction(
                                if (routine.active) {
                                    RoutineListAction.OnDeactivateRoutine(routine.id)
                                } else {
                                    RoutineListAction.OnActivateRoutine(routine.id)
                                }
                            )
                        },
                        onSimulateClick = {
                            onAction(RoutineListAction.OnSimulateRoutine(routine.id))
                        },
                        onDeleteClick = {
                            onDeleteRequest(routine.id)
                        },
                    )
                }
            }
        }
    }
    }
}

@Composable
private fun RoutineListHeader(
    compact: Boolean,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (!compact && maxWidth >= 640.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                RoutineListTitle(modifier = Modifier.weight(1f))
                HeaderActions(
                    compact = false,
                    onBackClick = onBackClick,
                    onCreateClick = onCreateClick,
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                RoutineListTitle()
                HeaderActions(
                    compact = compact,
                    onBackClick = onBackClick,
                    onCreateClick = onCreateClick,
                )
            }
        }
    }
}

@Composable
private fun RoutineListTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Rotinas do Smart Pot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = "Gerencie rotinas logicas de irrigacao e iluminacao deste Smart Pot.",
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun HeaderActions(
    compact: Boolean,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    if (compact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                onClick = onCreateClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LeafOnColors.GreenPrimary,
                    contentColor = LeafOnColors.TextOnDark,
                ),
            ) {
                Text("Nova rotina")
            }
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Voltar")
            }
        }
        return
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(
            onClick = onBackClick,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("Voltar")
        }
        Button(
            onClick = onCreateClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            Text("Nova rotina")
        }
    }
}

@Composable
private fun RoutineListLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
    }
}

@Composable
private fun RoutineListErrorState(
    message: String,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Nao foi possivel carregar as rotinas.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
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
            ),
        ) {
            Text("Tentar novamente")
        }
    }
}

@Composable
private fun RoutineListEmptyState(
    onCreateClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Nenhuma rotina cadastrada.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Crie a primeira rotina logica para este Smart Pot.",
            style = MaterialTheme.typography.bodyMedium,
            color = LeafOnColors.TextSecondary,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onCreateClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            Text("Criar rotina")
        }
    }
}

@Composable
private fun RoutineListFeedback(
    message: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
        )
    }
}

private fun routineListTopBarState(state: RoutineListState): AppTopBarState {
    val enabledRoutines = state.routines.count { it.active }
    val totalRoutines = state.routines.size

    return AppTopBarState(
        title = "Rotinas",
        subject = "$enabledRoutines/$totalRoutines rotinas ativas",
        subjectOnline = enabledRoutines > 0,
        lastUpdateLabel = "Configuracao logica do sistema",
    )
}
