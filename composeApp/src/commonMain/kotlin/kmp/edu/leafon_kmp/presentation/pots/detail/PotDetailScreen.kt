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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.data.repository.TelemetryRepository
import kmp.edu.leafon_kmp.data.repository.TelemetryRepositoryMemory
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryRepository
import kmp.edu.leafon_kmp.core.bluetooth.NoOpBluetoothTelemetryRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotDetailHeader
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotQuickActions
import kmp.edu.leafon_kmp.presentation.pots.detail.components.PotTelemetrySection
import kmp.edu.leafon_kmp.presentation.pots.detail.components.BluetoothTelemetrySection

@Composable
fun PotDetailScreen(
    potId: String,
    smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
    telemetryRepository: TelemetryRepository = TelemetryRepositoryMemory(),
    bluetoothTelemetryRepository: BluetoothTelemetryRepository =
        NoOpBluetoothTelemetryRepository(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteSuccess: () -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val viewModel = remember(
        potId,
        smartPotRepository,
        telemetryRepository,
        bluetoothTelemetryRepository,
    ) {
        PotDetailViewModel(
            potId = potId,
            smartPotRepository = smartPotRepository,
            telemetryRepository = telemetryRepository,
            bluetoothTelemetryRepository = bluetoothTelemetryRepository,
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
            Column(modifier = Modifier.fillMaxSize()) {
                AppTopBar(
                    state = potDetailTopBarState(viewModel.state),
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
                    state = viewModel.state,
                    onAction = viewModel::onAction,
                    onBackClick = onBackClick,
                    onEditClick = onEditClick,
                    onDeleteClick = {
                        showDeleteDialog = true
                    },
                    onViewRoutinesClick = onViewRoutinesClick,
                    onViewAlertsClick = onViewAlertsClick,
                    modifier = Modifier.weight(1f),
                )
            }
        } else {
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
                        state = potDetailTopBarState(viewModel.state),
                        onNotificationsClick = onNotificationsClick,
                        onProfileClick = onProfileClick,
                    )

                    PotDetailContent(
                        state = viewModel.state,
                        onAction = viewModel::onAction,
                        onBackClick = onBackClick,
                        onEditClick = onEditClick,
                        onDeleteClick = {
                            showDeleteDialog = true
                        },
                        onViewRoutinesClick = onViewRoutinesClick,
                        onViewAlertsClick = onViewAlertsClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text("Excluir vaso")
                },
                text = {
                    Text("Deseja excluir ${viewModel.state.plantName}? Esta acao nao pode ser desfeita.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deletePot(onDeleteSuccess)
                        },
                    ) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
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
private fun PotDetailContent(
    state: PotDetailState,
    onAction: (PotDetailAction) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
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
                onBackClick = onBackClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
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
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onViewRoutinesClick: (String) -> Unit,
    onViewAlertsClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        PotDetailBackButton(
            onBackClick = onBackClick,
        )

        PotDetailIntro()

        PotDetailHeader(
            plantName = state.plantName,
            deviceId = state.deviceId,
            humidityMin = state.humidityMin,
        )

        PotTelemetrySection(
            latestTelemetry = state.latestTelemetry,
            isTelemetryLoading = state.isTelemetryLoading,
            telemetryErrorMessage = state.telemetryErrorMessage,
            humidityMin = state.humidityMin,
            deviceId = state.deviceId,
        )

        BluetoothTelemetrySection(
            devices = state.pairedBluetoothDevices,
            selectedAddress = state.selectedBluetoothAddress,
            connectionStatus = state.bluetoothConnectionStatus,
            latestReading = state.latestBluetoothReading,
            isLoadingDevices = state.isLoadingBluetoothDevices,
            isSyncing = state.isSyncingBluetoothTelemetry,
            errorMessage = state.bluetoothErrorMessage,
            feedbackMessage = state.bluetoothFeedbackMessage,
            onDeviceSelected = {
                onAction(PotDetailAction.OnBluetoothDeviceSelected(it))
            },
            onReloadDevices = {
                onAction(PotDetailAction.OnReloadBluetoothDevices)
            },
            onConnect = {
                onAction(PotDetailAction.OnConnectBluetoothClick)
            },
            onDisconnect = {
                onAction(PotDetailAction.OnDisconnectBluetoothClick)
            },
            onSync = {
                onAction(PotDetailAction.OnSyncBluetoothTelemetryClick)
            },
        )

        PotQuickActions(
            onEditClick = {
                onAction(PotDetailAction.OnEditClick)
                onEditClick(state.potId)
            },
            onDeleteClick = onDeleteClick,
            onViewRoutinesClick = {
                onAction(PotDetailAction.OnViewRoutinesClick)
                onViewRoutinesClick(state.potId)
            },
            onViewAlertsClick = {
                onAction(PotDetailAction.OnViewAlertsClick)
                onViewAlertsClick(state.potId)
            },
            isDeleting = state.isDeleting,
        )
    }
}

@Composable
private fun PotDetailBackButton(
    onBackClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onBackClick,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LeafOnColors.TextPrimary,
        ),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = null,
        )
        Text("Voltar")
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
            text = "Veja os dados reais do SmartPot e acesse as principais acoes.",
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
    val subject = state.plantName.ifBlank { "Smart Pot" }

    return AppTopBarState(
        title = "Detalhe do pot",
        subject = subject,
        subjectOnline = state.deviceId != null,
        lastUpdateLabel = "Dados do Smart Pot",
    )
}
