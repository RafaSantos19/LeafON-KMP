package kmp.edu.leafon_kmp.data.remote.dto

import kmp.edu.leafon_kmp.data.mapper.toDomain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LatestTelemetryResponseDtoTest {

    @Test
    fun decodesCurrentLatestTelemetryResponse() {
        val dto = Json.decodeFromString<LatestTelemetryResponseDto>(
            """
                {
                  "soilHumidity": 57,
                  "airHumidity": 58.3,
                  "temperature": 21.2,
                  "luminosityStatus": "ESCURO"
                }
            """.trimIndent()
        )

        val reading = dto.toDomain("pot-1")

        assertEquals(57, reading.soilHumidity)
        assertEquals(58.3, reading.airHumidity)
        assertEquals(21.2, reading.temperature)
        assertEquals("ESCURO", reading.luminosityStatus)
        assertEquals("pot-1", reading.smartPotId)
        assertNull(reading.readAt)
    }
}
