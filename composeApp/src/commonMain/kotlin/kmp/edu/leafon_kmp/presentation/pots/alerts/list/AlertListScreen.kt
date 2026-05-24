package kmp.edu.leafon_kmp.presentation.pots.alerts.list

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.data.repository.AlertRepository
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.alerts.components.AlertCard

@Composable
fun AlertListScreen(
    potId: String,
    alertRepository: AlertRepository,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId, alertRepository) {
        AlertListViewModel(
            potId = potId,
            alertRepository = alertRepository,
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
            CompactAlertListLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        } else {
            ExpandedAlertListLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
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
private fun ExpandedAlertListLayout(
    state: AlertListState,
    onAction: (AlertListAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.ALERTS,
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
                state = alertListTopBarState(state),
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )

            AlertListContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactAlertListLayout(
    state: AlertListState,
    onAction: (AlertListAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = alertListTopBarState(state),
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.ALERTS,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        AlertListContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AlertListContent(
    state: AlertListState,
    onAction: (AlertListAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        AlertListHeader(
            state = state,
            onFilterChange = { showUnreadOnly ->
                onAction(AlertListAction.OnUnreadFilterChange(showUnreadOnly))
            },
            onBackClick = {
                onAction(AlertListAction.OnBackClick)
                onBackClick()
            },
        )

        state.successMessage?.let { message ->
            AlertListFeedbackCard(
                message = message,
                containerColor = LeafOnColors.Success.copy(alpha = 0.12f),
                contentColor = LeafOnColors.Success,
            )
        }

        state.errorMessage?.let { message ->
            if (!state.isLoading) {
                AlertListFeedbackCard(
                    message = message,
                    containerColor = LeafOnColors.Error.copy(alpha = 0.12f),
                    contentColor = LeafOnColors.Error,
                )
            }
        }

        when {
            state.isLoading -> AlertListLoadingState()
            state.errorMessage != null && state.alerts.isEmpty() -> AlertListErrorState(
                message = state.errorMessage.orEmpty(),
                onRetryClick = { onAction(AlertListAction.OnRetryClick) },
            )
            state.alerts.isEmpty() -> AlertListEmptyState(
                showUnreadOnly = state.showUnreadOnly,
                potId = state.potId,
            )
            else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.alerts.forEach { alert ->
                    AlertCard(
                        alert = alert,
                        onMarkAsReadClick = { alertId ->
                            onAction(AlertListAction.OnMarkAsReadClick(alertId))
                        },
                        isUpdating = state.isMarkingAsRead && state.markingAlertId == alert.id,
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertListHeader(
    state: AlertListState,
    onFilterChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth >= 640.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AlertListTitle(
                    potId = state.potId,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Voltar")
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                AlertListTitle(potId = state.potId)
                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Voltar")
                }
            }
        }
    }

    AlertFilterRow(
        showUnreadOnly = state.showUnreadOnly,
        onFilterChange = onFilterChange,
    )
}

@Composable
private fun AlertFilterRow(
    showUnreadOnly: Boolean,
    onFilterChange: (Boolean) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        AlertFilterButton(
            label = "Todos",
            selected = !showUnreadOnly,
            onClick = { onFilterChange(false) },
        )
        AlertFilterButton(
            label = "Nao lidos",
            selected = showUnreadOnly,
            onClick = { onFilterChange(true) },
        )
    }
}

@Composable
private fun AlertFilterButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    if (selected) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            Text(label)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(label)
        }
    }
}

@Composable
private fun AlertListTitle(
    potId: String,
    modifier: Modifier = Modifier,
) {
    val isPotSpecific = potId.isNotBlank()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = if (isPotSpecific) "Alertas do pot" else "Alertas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = if (isPotSpecific) {
                "Acompanhe eventos deste Smart Pot e marque os alertas como lidos."
            } else {
                "Veja os alertas da sua conta e filtre apenas os nao lidos."
            },
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun AlertListLoadingState() {
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
private fun AlertListErrorState(
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
            text = "Nao foi possivel carregar os alertas.",
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
private fun AlertListFeedbackCard(
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

@Composable
private fun AlertListEmptyState(
    showUnreadOnly: Boolean,
    potId: String,
) {
    val isPotSpecific = potId.isNotBlank()
    val title = when {
        showUnreadOnly -> "Nenhum alerta nao lido."
        isPotSpecific -> "Nenhum alerta registrado para este pot."
        else -> "Nenhum alerta registrado."
    }
    val description = when {
        showUnreadOnly -> "Todos os alertas ja foram marcados como lidos."
        isPotSpecific -> "Este Smart Pot ainda nao gerou alertas."
        else -> "Sua conta ainda nao possui ocorrencias recentes."
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = LeafOnColors.TextSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

private fun alertListTopBarState(state: AlertListState): AppTopBarState {
    val activeAlerts = state.alerts.count { it.status == AlertStatus.PENDING }
    val subject = when {
        state.isLoading -> "Carregando alertas"
        activeAlerts > 0 -> "$activeAlerts alertas pendentes"
        state.showUnreadOnly -> "Sem alertas nao lidos"
        else -> "Sem alertas pendentes"
    }

    return AppTopBarState(
        title = "Alertas",
        subject = subject,
        subjectOnline = activeAlerts == 0,
        lastUpdateLabel = if (state.potId.isBlank()) {
            "Eventos recentes da conta"
        } else {
            "Eventos recentes do pot"
        },
    )
}
