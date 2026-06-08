package kmp.edu.leafon_kmp.presentation.pots.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Sync
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothConnectionStatus
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kmp.edu.leafon_kmp.core.bluetooth.PairedBluetoothDevice
import kmp.edu.leafon_kmp.core.format.format
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun BluetoothTelemetrySection(
    devices: List<PairedBluetoothDevice>,
    selectedAddress: String?,
    connectionStatus: BluetoothConnectionStatus,
    latestReading: BluetoothTelemetryReading?,
    isLoadingDevices: Boolean,
    isSyncing: Boolean,
    errorMessage: String?,
    feedbackMessage: String?,
    onDeviceSelected: (String) -> Unit,
    onReloadDevices: () -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onSync: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Telemetria Bluetooth",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )

        ConnectionStatus(
            status = connectionStatus,
            errorMessage = errorMessage,
            feedbackMessage = feedbackMessage,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Dispositivos pareados",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
            OutlinedButton(
                onClick = onReloadDevices,
                enabled = !isLoadingDevices,
            ) {
                if (isLoadingDevices) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                    )
                    Text("Atualizar")
                }
            }
        }

        if (devices.isEmpty() && !isLoadingDevices) {
            Text(
                text = if (connectionStatus == BluetoothConnectionStatus.UNAVAILABLE) {
                    "Bluetooth nao esta disponivel neste target."
                } else {
                    "Nenhum dispositivo pareado encontrado."
                },
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                devices.forEach { device ->
                    if (device.address == selectedAddress) {
                        Button(
                            onClick = { onDeviceSelected(device.address) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LeafOnColors.GreenPrimary,
                                contentColor = LeafOnColors.TextOnDark,
                            ),
                        ) {
                            Text(device.name)
                        }
                    } else {
                        OutlinedButton(onClick = { onDeviceSelected(device.address) }) {
                            Text(device.name)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (
                connectionStatus == BluetoothConnectionStatus.CONNECTED ||
                connectionStatus == BluetoothConnectionStatus.CONNECTING
            ) {
                OutlinedButton(
                    onClick = onDisconnect,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Desconectar")
                }
            } else {
                Button(
                    onClick = onConnect,
                    enabled = selectedAddress != null &&
                        connectionStatus != BluetoothConnectionStatus.UNAVAILABLE,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.GreenPrimary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                ) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Outlined.Bluetooth,
                        contentDescription = null,
                    )
                    Text(
                        if (connectionStatus == BluetoothConnectionStatus.CONNECTING) {
                            "Conectando"
                        } else {
                            "Conectar Bluetooth"
                        }
                    )
                }
            }
        }

        BluetoothReadingGrid(reading = latestReading)

        Button(
            onClick = onSync,
            enabled = latestReading != null && !isSyncing,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.TextPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    color = LeafOnColors.TextOnDark,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = null,
                )
                Text("Sincronizar Telemetria")
            }
        }
    }
}

@Composable
private fun ConnectionStatus(
    status: BluetoothConnectionStatus,
    errorMessage: String?,
    feedbackMessage: String?,
) {
    val statusLabel = when (status) {
        BluetoothConnectionStatus.UNAVAILABLE -> "Indisponivel"
        BluetoothConnectionStatus.DISCONNECTED -> "Desconectado"
        BluetoothConnectionStatus.CONNECTING -> "Conectando"
        BluetoothConnectionStatus.CONNECTED -> "Conectado"
        BluetoothConnectionStatus.ERROR -> "Erro"
    }
    val tint = when (status) {
        BluetoothConnectionStatus.CONNECTED -> LeafOnColors.Success
        BluetoothConnectionStatus.ERROR -> LeafOnColors.Warning
        else -> LeafOnColors.TextSecondary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(tint.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Status: $statusLabel",
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
            )
            (errorMessage ?: feedbackMessage)?.let { message ->
                Text(
                    text = message,
                    fontSize = 13.sp,
                    color = LeafOnColors.TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun BluetoothReadingGrid(reading: BluetoothTelemetryReading?) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BluetoothValue("Umidade do solo", reading?.let { "${it.soilHumidity}%" } ?: "-")
        BluetoothValue("Valor bruto", reading?.soilHumidityRaw?.toString() ?: "-")
        BluetoothValue(
            "Umidade do ar",
            reading?.let { "${it.airHumidity.format(1)}%" } ?: "-",
        )
        BluetoothValue(
            "Temperatura",
            reading?.let { "${it.temperature.format(1)} C" } ?: "-",
        )
        BluetoothValue(
            "Luminosidade",
            reading?.let { "${it.luminosityStatus} (${it.luminosityDigital})" } ?: "-",
        )
        BluetoothValue("Recebida em", reading?.receivedAt ?: "-")
    }
}

@Composable
private fun BluetoothValue(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LeafOnColors.BgMain, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )
        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )
    }
}
