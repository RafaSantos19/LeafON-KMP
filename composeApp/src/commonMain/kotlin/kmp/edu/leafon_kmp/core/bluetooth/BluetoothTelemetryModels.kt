package kmp.edu.leafon_kmp.core.bluetooth

import kotlinx.serialization.Serializable

data class PairedBluetoothDevice(
    val name: String,
    val address: String,
)

enum class BluetoothConnectionStatus {
    UNAVAILABLE,
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR,
}

@Serializable
data class BluetoothTelemetryReading(
    val soilHumidity: Int,
    val soilHumidityRaw: Int,
    val airHumidity: Double,
    val temperature: Double,
    val luminosityStatus: String,
    val luminosityDigital: Int,
    val receivedAt: String = "",
)
