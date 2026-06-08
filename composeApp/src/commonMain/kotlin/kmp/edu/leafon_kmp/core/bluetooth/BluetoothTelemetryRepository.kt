package kmp.edu.leafon_kmp.core.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothTelemetryRepository {
    val connectionStatus: StateFlow<BluetoothConnectionStatus>
    val latestReading: StateFlow<BluetoothTelemetryReading?>
    val errorMessage: StateFlow<String?>

    suspend fun listPairedDevices(): List<PairedBluetoothDevice>
    suspend fun connect(address: String)
    suspend fun disconnect()
}

expect fun createBluetoothTelemetryRepository(): BluetoothTelemetryRepository
