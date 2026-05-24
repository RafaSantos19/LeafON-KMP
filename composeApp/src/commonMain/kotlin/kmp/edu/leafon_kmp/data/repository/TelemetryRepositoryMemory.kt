package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.core.network.ApiException

class TelemetryRepositoryMemory : TelemetryRepository {
    private val readingsByPotId = mutableMapOf<String, MutableList<TelemetryReading>>()
    private var nextId = 1

    override suspend fun createTelemetry(
        smartPotId: String,
        soilHumidity: Int,
        temperature: Double,
        luminosity: Double,
        readAt: String,
    ): TelemetryReading {
        if (smartPotId.isBlank()) {
            throw ApiException(404, "Vaso nao encontrado.")
        }
        if (soilHumidity !in 0..100 || readAt.isBlank()) {
            throw ApiException(400, "Leitura invalida.")
        }

        val created = TelemetryReading(
            id = nextId.toString(),
            smartPotId = smartPotId,
            soilHumidity = soilHumidity,
            temperature = temperature,
            luminosity = luminosity,
            readAt = readAt,
            createdAt = readAt,
        )

        nextId += 1
        val readings = readingsByPotId.getOrPut(smartPotId) { mutableListOf() }
        readings.add(0, created)
        return created
    }

    override suspend fun getTelemetry(smartPotId: String): List<TelemetryReading> {
        return readingsByPotId[smartPotId]?.toList().orEmpty()
    }

    override suspend fun getLatestTelemetry(smartPotId: String): TelemetryReading? {
        return readingsByPotId[smartPotId]?.firstOrNull()
    }
}
