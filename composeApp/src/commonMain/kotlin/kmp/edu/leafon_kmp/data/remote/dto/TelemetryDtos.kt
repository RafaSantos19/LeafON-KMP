package kmp.edu.leafon_kmp.data.remote.dto

import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kotlinx.serialization.Serializable

@Serializable
data class TelemetryResponseDto(
    val id: String? = null,
    val smartPotId: String? = null,
    val soilHumidity: Int? = null,
    val airHumidity: Double? = null,
    val temperature: Double? = null,
    val luminosity: Double? = null,
    val luminosityStatus: String? = null,
    val readAt: String? = null,
    val createdAt: String? = null,
)

@Serializable
data class LatestTelemetryResponseDto(
    val soilHumidity: Int,
    val airHumidity: Double,
    val temperature: Double,
    val luminosityStatus: String,
    val readAt: String? = null,
)

@Serializable
data class CreateTelemetryRequestDto(
    val smartPotId: String,
    val soilHumidity: Int,
    val temperature: Double,
    val luminosity: Double,
    val readAt: String,
)

@Serializable
data class SyncBluetoothTelemetryRequestDto(
    val soilHumidity: Int,
    val soilHumidityRaw: Int,
    val airHumidity: Double,
    val temperature: Double,
    val luminosityStatus: String,
    val luminosityDigital: Int,
)

fun BluetoothTelemetryReading.toSyncRequest(): SyncBluetoothTelemetryRequestDto =
    SyncBluetoothTelemetryRequestDto(
        soilHumidity = soilHumidity,
        soilHumidityRaw = soilHumidityRaw,
        airHumidity = airHumidity,
        temperature = temperature,
        luminosityStatus = luminosityStatus,
        luminosityDigital = luminosityDigital,
    )
