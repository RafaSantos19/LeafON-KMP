package kmp.edu.leafon_kmp.core.bluetooth

import kotlinx.serialization.json.Json

private val bluetoothTelemetryJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

fun parseBluetoothTelemetryReading(
    line: String,
    receivedAt: String,
): BluetoothTelemetryReading? {
    if (line.isBlank()) return null

    return runCatching {
        bluetoothTelemetryJson.decodeFromString<BluetoothTelemetryReading>(line)
            .copy(receivedAt = receivedAt)
    }.getOrNull()
}
