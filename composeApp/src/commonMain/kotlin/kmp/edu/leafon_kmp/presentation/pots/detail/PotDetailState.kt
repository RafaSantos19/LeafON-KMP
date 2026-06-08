package kmp.edu.leafon_kmp.presentation.pots.detail

import kmp.edu.leafon_kmp.core.bluetooth.BluetoothConnectionStatus
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kmp.edu.leafon_kmp.core.bluetooth.PairedBluetoothDevice
import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading

data class PotDetailState(
    val potId: String,
    val plantName: String = "",
    val humidityMin: Int? = null,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isLoading: Boolean = true,
    val isTelemetryLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val latestTelemetry: LatestTelemetryReading? = null,
    val errorMessage: String? = null,
    val telemetryErrorMessage: String? = null,
    val pairedBluetoothDevices: List<PairedBluetoothDevice> = emptyList(),
    val selectedBluetoothAddress: String? = null,
    val bluetoothConnectionStatus: BluetoothConnectionStatus =
        BluetoothConnectionStatus.DISCONNECTED,
    val latestBluetoothReading: BluetoothTelemetryReading? = null,
    val isLoadingBluetoothDevices: Boolean = false,
    val isSyncingBluetoothTelemetry: Boolean = false,
    val bluetoothErrorMessage: String? = null,
    val bluetoothFeedbackMessage: String? = null,
)
