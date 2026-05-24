package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.TelemetryReading

interface TelemetryRepository {
    suspend fun createTelemetry(
        smartPotId: String,
        soilHumidity: Int,
        temperature: Double,
        luminosity: Double,
        readAt: String,
    ): TelemetryReading

    suspend fun getTelemetry(smartPotId: String): List<TelemetryReading>

    suspend fun getLatestTelemetry(smartPotId: String): TelemetryReading?
}
