package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading
import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.data.mapper.toDomain
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient
import kmp.edu.leafon_kmp.data.remote.dto.CreateTelemetryRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.toSyncRequest

class TelemetryRepositoryHttp(
    private val apiClient: LeafOnApiClient,
) : TelemetryRepository {

    override suspend fun createTelemetry(
        smartPotId: String,
        soilHumidity: Int,
        temperature: Double,
        luminosity: Double,
        readAt: String,
    ): TelemetryReading {
        return apiClient.createTelemetry(
            CreateTelemetryRequestDto(
                smartPotId = smartPotId,
                soilHumidity = soilHumidity,
                temperature = temperature,
                luminosity = luminosity,
                readAt = readAt,
            )
        ).toDomain()
    }

    override suspend fun getTelemetry(
        smartPotId: String,
        limit: Int?,
    ): List<TelemetryReading> {
        return apiClient.getTelemetry(
            smartPotId = smartPotId,
            limit = limit,
        ).mapNotNull { response ->
            runCatching {
                response.toDomain(smartPotIdFallback = smartPotId)
            }.getOrNull()
        }
    }

    override suspend fun getLatestTelemetry(smartPotId: String): LatestTelemetryReading? {
        return apiClient.getLatestTelemetry(smartPotId)?.toDomain(smartPotId)
    }

    override suspend fun syncBluetoothTelemetry(
        smartPotId: String,
        reading: BluetoothTelemetryReading,
    ) {
        apiClient.syncBluetoothTelemetry(
            smartPotId = smartPotId,
            request = reading.toSyncRequest(),
        )
    }
}
