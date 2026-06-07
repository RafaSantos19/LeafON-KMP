package kmp.edu.leafon_kmp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.format.format
import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.core.time.ReadableTimestampFormatter
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.home.components.ChartRange
import kmp.edu.leafon_kmp.presentation.home.components.ChartRangeSelector
import kmp.edu.leafon_kmp.presentation.home.components.LineChartCard
import kmp.edu.leafon_kmp.presentation.home.components.MetricCard
import kmp.edu.leafon_kmp.presentation.home.model.MetricUi

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
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactDashboardLayout(
                state = state,
                onAction = onAction,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
            )
        } else {
            ExpandedDashboardLayout(
                state = state,
                onAction = onAction,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
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
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.HOME,
            onHomeClick = {},
            onPlantAndPotClick = { onPotsClick?.invoke() },
            onAlertsClick = { onAlertsClick?.invoke() },
            onProfileClick = { onProfileClick?.invoke() },
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LeafOnColors.BorderDefault),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                state = dashboardTopBarState(state),
                onNotificationsClick = {},
                onProfileClick = { onProfileClick?.invoke() },
            )

            DashboardContent(
                state = state,
                onAction = onAction,
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
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = dashboardTopBarState(state),
            onNotificationsClick = {},
            onProfileClick = { onProfileClick?.invoke() },
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.HOME,
            onHomeClick = {},
            onPlantAndPotClick = { onPotsClick?.invoke() },
            onAlertsClick = { onAlertsClick?.invoke() },
            onProfileClick = { onProfileClick?.invoke() },
            compact = true,
        )

        DashboardContent(
            state = state,
            onAction = onAction,
            isCompact = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun DashboardContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.smartPots.isEmpty() && !state.isLoading && state.errorMessage == null -> {
                DashboardEmptyState(
                    title = "Voce ainda nao cadastrou nenhum vaso.",
                    description = "Cadastre um Smart Pot para acompanhar metricas, telemetria e alertas.",
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    HomeHeader(
                        state = state,
                        onRefreshClick = { onAction(HomeAction.OnRefreshClick) },
                        isCompact = isCompact,
                    )
                    SmartPotSelector(
                        smartPots = state.smartPots,
                        selectedSmartPotId = state.selectedSmartPotId,
                        onPotSelected = { onAction(HomeAction.OnSmartPotSelected(it)) },
                    )
                    MetricGrid(
                        state = state,
                        isCompact = isCompact,
                    )
                    TelemetrySection(
                        state = state,
                        onRangeSelected = { onAction(HomeAction.OnRangeSelected(it)) },
                        isCompact = isCompact,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        when {
            state.isLoading -> DashboardLoadingState()
            state.errorMessage != null -> DashboardErrorState(
                message = state.errorMessage,
                onRetryClick = { onAction(HomeAction.OnRetryClick) },
            )
        }
    }
}

private fun dashboardTopBarState(state: HomeState): AppTopBarState {
    val selectedPot = state.smartPots.firstOrNull { it.id == state.selectedSmartPotId }
    val subject = selectedPot?.plantName ?: "Nenhum Smart Pot selecionado"
    val lastUpdate = ReadableTimestampFormatter.formatOrFallback(state.latestTelemetry?.readAt)
        ?: "Sem telemetria"

    return AppTopBarState(
        title = "Inicio",
        subject = subject,
        subjectOnline = selectedPot?.deviceId != null,
        lastUpdateLabel = "Ultima leitura: $lastUpdate",
    )
}

@Composable
private fun HomeHeader(
    state: HomeState,
    onRefreshClick: () -> Unit,
    isCompact: Boolean,
) {
    val selectedPot = state.smartPots.firstOrNull { it.id == state.selectedSmartPotId }
    val description = selectedPot?.let {
        "Acompanhe a telemetria, o historico e os alertas de ${it.plantName}."
    } ?: "Selecione um vaso para visualizar os dados reais do dashboard."

    if (isCompact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Dashboard do Smart Pot",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = LeafOnColors.TextPrimary,
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = LeafOnColors.TextSecondary,
            )
            OutlinedButton(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Atualizar")
            }
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Dashboard do Smart Pot",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = LeafOnColors.TextPrimary,
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        OutlinedButton(onClick = onRefreshClick) {
            Text("Atualizar")
        }
    }
}

@Composable
private fun SmartPotSelector(
    smartPots: List<SmartPot>,
    selectedSmartPotId: String?,
    onPotSelected: (String) -> Unit,
) {
    if (smartPots.isEmpty()) {
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        smartPots.forEach { smartPot ->
            val selected = smartPot.id == selectedSmartPotId
            if (selected) {
                Button(
                    onClick = { onPotSelected(smartPot.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.GreenPrimary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                ) {
                    Text(smartPot.plantName)
                }
            } else {
                OutlinedButton(onClick = { onPotSelected(smartPot.id) }) {
                    Text(smartPot.plantName)
                }
            }
        }
    }
}

@Composable
private fun MetricGrid(
    state: HomeState,
    isCompact: Boolean,
) {
    val metrics = state.toMetrics()

    if (isCompact) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            metrics.forEach { metric ->
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
            metrics.forEach { metric ->
                MetricCard(
                    metric = metric,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun TelemetrySection(
    state: HomeState,
    onRangeSelected: (ChartRange) -> Unit,
    isCompact: Boolean,
) {
    val chartData = state.selectedChartData()
    val emptyMessage = if (state.selectedSmartPotId == null) {
        "Selecione um vaso para ver o historico."
    } else {
        "Nenhuma leitura registrada ainda."
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isCompact) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Historico de telemetria",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                ChartRangeSelector(
                    selectedRange = state.selectedRange,
                    onRangeSelected = onRangeSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Historico de telemetria",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                ChartRangeSelector(
                    selectedRange = state.selectedRange,
                    onRangeSelected = onRangeSelected,
                )
            }
        }

        state.telemetryErrorMessage?.let { message ->
            TelemetryFeedbackCard(message = message)
        }

        if (state.isTelemetryLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
            }
            return
        }

        LineChartCard(
            title = "Umidade do solo",
            values = chartData.map { it.soilHumidity.toFloat() },
            valueSuffix = "%",
            emptyMessage = emptyMessage,
        )
        LineChartCard(
            title = "Temperatura",
            values = chartData.map { it.temperature.toFloat() },
            valueSuffix = "°C",
            emptyMessage = emptyMessage,
            color = LeafOnColors.Warning,
        )
        LineChartCard(
            title = "Luminosidade",
            values = chartData.map { it.luminosity.toFloat() },
            valueSuffix = " lux",
            emptyMessage = emptyMessage,
            color = LeafOnColors.TextPrimary,
        )
    }
}

@Composable
private fun TelemetryFeedbackCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(LeafOnColors.Warning.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text(
            text = message,
            fontSize = 14.sp,
            color = LeafOnColors.TextPrimary,
        )
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
                text = "Nao foi possivel carregar o dashboard.",
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
                ),
            ) {
                Text("Tentar novamente")
            }
        }
    }
}

@Composable
private fun DashboardEmptyState(
    title: String,
    description: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = LeafOnColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
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
}

private fun HomeState.selectedChartData(): List<TelemetryReading> {
    return telemetryHistory
        .take(selectedRange.pointLimit)
        .reversed()
}

private fun HomeState.toMetrics(): List<MetricUi> {
    return listOf(
        MetricUi(
            label = "Total de vasos",
            value = smartPots.size.toString(),
            unit = "",
        ),
        MetricUi(
            label = "Alertas nao lidos",
            value = unreadAlertsCount.toString(),
            unit = "",
        ),
        MetricUi(
            label = "Umidade atual",
            value = latestTelemetry?.soilHumidity?.toString() ?: "--",
            unit = if (latestTelemetry != null) "%" else "",
        ),
        MetricUi(
            label = "Temperatura atual",
            value = latestTelemetry?.temperature?.format(1) ?: "--",
            unit = if (latestTelemetry != null) "°C" else "",
        ),
        MetricUi(
            label = "Luminosidade atual",
            value = latestTelemetry?.luminosity?.format(1) ?: "--",
            unit = if (latestTelemetry != null) "lux" else "",
        ),
    )
}
