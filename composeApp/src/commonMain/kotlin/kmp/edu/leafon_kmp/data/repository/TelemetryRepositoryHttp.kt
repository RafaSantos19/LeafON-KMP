package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.data.mapper.toDomain
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient
import kmp.edu.leafon_kmp.data.remote.dto.CreateTelemetryRequestDto

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

    override suspend fun getTelemetry(smartPotId: String): List<TelemetryReading> {
        return apiClient.getTelemetry(smartPotId).map { it.toDomain() }
    }

    override suspend fun getLatestTelemetry(smartPotId: String): TelemetryReading? {
        return apiClient.getLatestTelemetry(smartPotId)?.toDomain()
    }
}
