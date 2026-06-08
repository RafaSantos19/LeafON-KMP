package kmp.edu.leafon_kmp.core.bluetooth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NoOpBluetoothTelemetryRepository : BluetoothTelemetryRepository {
    private val mutableConnectionStatus =
        MutableStateFlow(BluetoothConnectionStatus.UNAVAILABLE)
    private val mutableLatestReading = MutableStateFlow<BluetoothTelemetryReading?>(null)
    private val mutableErrorMessage =
        MutableStateFlow<String?>("Bluetooth disponivel apenas no Android.")

    override val connectionStatus: StateFlow<BluetoothConnectionStatus> =
        mutableConnectionStatus
    override val latestReading: StateFlow<BluetoothTelemetryReading?> = mutableLatestReading
    override val errorMessage: StateFlow<String?> = mutableErrorMessage

    override suspend fun listPairedDevices(): List<PairedBluetoothDevice> = emptyList()

    override suspend fun connect(address: String) = Unit

    override suspend fun disconnect() = Unit
}
