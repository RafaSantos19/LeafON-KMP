package kmp.edu.leafon_kmp.presentation.pots.detail

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
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotDetailHeader
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotQuickActions
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotTelemetrySection
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

@Composable
fun PotDetailScreen(
    potId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId) {
        PotDetailViewModel(potId = potId)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactPotDetailLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onViewRoutinesClick = onViewRoutinesClick,
                onViewAlertsClick = onViewAlertsClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        } else {
            ExpandedPotDetailLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onViewRoutinesClick = onViewRoutinesClick,
                onViewAlertsClick = onViewAlertsClick,
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
private fun ExpandedPotDetailLayout(
    state: PotDetailState,
    onAction: (PotDetailAction) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
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
                state = potDetailTopBarState(state),
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )

            PotDetailContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onViewRoutinesClick = onViewRoutinesClick,
                onViewAlertsClick = onViewAlertsClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactPotDetailLayout(
    state: PotDetailState,
    onAction: (PotDetailAction) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = potDetailTopBarState(state),
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

        PotDetailContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            onEditClick = onEditClick,
            onViewRoutinesClick = onViewRoutinesClick,
            onViewAlertsClick = onViewAlertsClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PotDetailContent(
    state: PotDetailState,
    onAction: (PotDetailAction) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 24.dp),
    ) {
        when {
            state.isLoading -> PotDetailLoadingState()
            state.errorMessage != null -> PotDetailErrorState(
                message = state.errorMessage,
                onRetryClick = {
                    onAction(PotDetailAction.OnRetryClick)
                },
                onBackClick = onBackClick,
            )
            else -> PotDetailLoadedContent(
                state = state,
                onAction = onAction,
                onEditClick = onEditClick,
                onViewRoutinesClick = onViewRoutinesClick,
                onViewAlertsClick = onViewAlertsClick,
            )
        }
    }
}

@Composable
private fun PotDetailLoadedContent(
    state: PotDetailState,
    onAction: (PotDetailAction) -> Unit,
    onEditClick: (String) -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        PotDetailIntro()

        PotDetailHeader(
            name = state.name,
            plantName = state.plantName,
            status = state.status,
            lastUpdateLabel = state.lastUpdateLabel,
        )

        PotTelemetrySection(
            humidityPercent = state.humidityPercent,
            temperatureCelsius = state.temperatureCelsius,
            lightLevel = state.lightLevel,
        )

        PotQuickActions(
            onEditClick = {
                onAction(PotDetailAction.OnEditClick)
                onEditClick(state.potId)
            },
            onViewRoutinesClick = {
                onAction(PotDetailAction.OnViewRoutinesClick)
                onViewRoutinesClick(state.potId)
            },
            onViewAlertsClick = {
                onAction(PotDetailAction.OnViewAlertsClick)
                onViewAlertsClick(state.potId)
            },
        )
    }
}

@Composable
private fun PotDetailIntro() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Detalhe do pot",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
        Text(
            text = "Acompanhe o estado atual do vaso e acesse as principais acoes.",
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun PotDetailLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
    }
}

@Composable
private fun PotDetailErrorState(
    message: String,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Nao foi possivel carregar o pot.",
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
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = LeafOnColors.TextSecondary,
                    contentColor = LeafOnColors.TextOnDark,
                ),
            ) {
                Text("Voltar")
            }
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
}

private fun potDetailTopBarState(state: PotDetailState): AppTopBarState {
    val subject = state.name.ifBlank { "Smart Pot" }
    val lastUpdate = state.lastUpdateLabel.ifBlank { "carregando" }

    return AppTopBarState(
        title = "Detalhe do pot",
        subject = subject,
        subjectOnline = state.status == PotStatus.ONLINE,
        lastUpdateLabel = "Ultima atualizacao: $lastUpdate",
    )
}
