package kmp.edu.leafon_kmp.data.repository

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class TelemetryRepositoryMemoryTest {

    @Test
    fun getTelemetryRespectsRequestedLimit() = runBlocking {
        val repository = TelemetryRepositoryMemory()

        repeat(5) { index ->
            repository.createTelemetry(
                smartPotId = "pot-1",
                soilHumidity = 40 + index,
                temperature = 20.0 + index,
                luminosity = 100.0 + index,
                readAt = "2026-06-06T10:0${index}:00Z",
            )
        }

        val readings = repository.getTelemetry(
            smartPotId = "pot-1",
            limit = 2,
        )

        assertEquals(2, readings.size)
        assertEquals(listOf(44, 43), readings.map { it.soilHumidity })
    }
}
