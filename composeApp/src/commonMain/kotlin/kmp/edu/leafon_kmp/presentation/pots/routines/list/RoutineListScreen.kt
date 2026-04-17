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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.routines.components.RoutineCard

@Composable
fun RoutineListScreen(
    potId: String,
    repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
    onBackClick: () -> Unit,
    onCreateRoutineClick: (String) -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId, repositorio) {
        RoutineListViewModel(
            potId = potId,
            repositorio = repositorio,
        )
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
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        RoutineListHeader(
            onBackClick = onBackClick,
            onCreateClick = {
                onAction(RoutineListAction.OnCreateRoutineClick)
                onCreateRoutineClick(state.potId)
            },
        )

        when {
            state.isLoading -> RoutineListLoadingState()
            state.errorMessage != null -> RoutineListErrorState(
                message = state.errorMessage,
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
                        onToggleClick = {
                            onAction(RoutineListAction.OnToggleRoutine(routine.id))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RoutineListHeader(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth >= 640.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                RoutineListTitle(modifier = Modifier.weight(1f))
                HeaderActions(
                    onBackClick = onBackClick,
                    onCreateClick = onCreateClick,
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                RoutineListTitle()
                HeaderActions(
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
            text = "Rotinas de irrigacao",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = "Controle os horarios de irrigacao automatica deste Smart Pot.",
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun HeaderActions(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
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
            text = "Crie a primeira automacao de irrigacao para este pot.",
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

private fun routineListTopBarState(state: RoutineListState): AppTopBarState {
    val enabledRoutines = state.routines.count { it.enabled }
    val totalRoutines = state.routines.size

    return AppTopBarState(
        title = "Rotinas",
        subject = "$enabledRoutines/$totalRoutines rotinas ativas",
        subjectOnline = enabledRoutines > 0,
        lastUpdateLabel = "Automacao de irrigacao",
    )
}
