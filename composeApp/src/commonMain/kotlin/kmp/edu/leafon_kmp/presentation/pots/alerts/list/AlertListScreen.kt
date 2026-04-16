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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.alerts.components.AlertCard
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertSeverity
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertStatus

@Composable
fun AlertListScreen(
    potId: String,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId) {
        AlertListViewModel(potId = potId)
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
            onBackClick = {
                onAction(AlertListAction.OnBackClick)
                onBackClick()
            },
        )

        when {
            state.isLoading -> AlertListLoadingState()
            state.errorMessage != null -> AlertListErrorState(
                message = state.errorMessage,
                onRetryClick = { onAction(AlertListAction.OnRetryClick) },
            )
            state.alerts.isEmpty() -> AlertListEmptyState()
            else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.alerts.forEach { alert ->
                    AlertCard(alert = alert)
                }
            }
        }
    }
}

@Composable
private fun AlertListHeader(
    onBackClick: () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        if (maxWidth >= 640.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AlertListTitle(modifier = Modifier.weight(1f))
                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Voltar")
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                AlertListTitle()
                OutlinedButton(
                    onClick = onBackClick,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Voltar")
                }
            }
        }
    }
}

@Composable
private fun AlertListTitle(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Alertas do pot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = "Acompanhe eventos ativos e ocorrencias recentes deste Smart Pot.",
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
private fun AlertListEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Nenhum alerta registrado.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Este pot nao possui ocorrencias recentes.",
            style = MaterialTheme.typography.bodyMedium,
            color = LeafOnColors.TextSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

private fun alertListTopBarState(state: AlertListState): AppTopBarState {
    val activeAlerts = state.alerts.count { it.status == AlertStatus.ACTIVE }
    val criticalAlerts = state.alerts.count {
        it.status == AlertStatus.ACTIVE && it.severity == AlertSeverity.CRITICAL
    }
    val subject = when {
        criticalAlerts > 0 -> "$criticalAlerts alertas criticos"
        activeAlerts > 0 -> "$activeAlerts alertas ativos"
        else -> "Sem alertas ativos"
    }

    return AppTopBarState(
        title = "Alertas",
        subject = subject,
        subjectOnline = activeAlerts == 0,
        lastUpdateLabel = "Eventos recentes do pot",
    )
}
