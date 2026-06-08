package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading
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

    override suspend fun getTelemetry(
        smartPotId: String,
        limit: Int?,
    ): List<TelemetryReading> {
        val readings = readingsByPotId[smartPotId].orEmpty()
        return limit
            ?.coerceAtLeast(0)
            ?.let(readings::take)
            ?: readings.toList()
    }

    override suspend fun getLatestTelemetry(smartPotId: String): LatestTelemetryReading? {
        return readingsByPotId[smartPotId]?.firstOrNull()?.let { reading ->
            LatestTelemetryReading(
                smartPotId = reading.smartPotId,
                soilHumidity = reading.soilHumidity,
                airHumidity = 0.0,
                temperature = reading.temperature,
                luminosityStatus = reading.luminosityStatus
                    ?: reading.luminosity?.toString()
                    ?: "Nao informado",
                readAt = reading.readAt,
            )
        }
    }

    override suspend fun syncBluetoothTelemetry(
        smartPotId: String,
        reading: BluetoothTelemetryReading,
    ) = Unit
}
