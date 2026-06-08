package kmp.edu.leafon_kmp.data.remote.dto

import kmp.edu.leafon_kmp.data.mapper.toDomain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TelemetryHistoryResponseDtoTest {

    @Test
    fun decodesCurrentHistoryItemWithoutNumericLuminosity() {
        val dto = Json.decodeFromString<TelemetryResponseDto>(
            """
                {
                  "soilHumidity": 57,
                  "airHumidity": 58.3,
                  "temperature": 21.2,
                  "luminosityStatus": "ESCURO",
                  "createdAt": "2026-06-07T01:00:00Z"
                }
            """.trimIndent()
        )

        val reading = dto.toDomain(smartPotIdFallback = "pot-1")

        assertEquals("pot-1", reading.smartPotId)
        assertEquals(58.3, reading.airHumidity)
        assertEquals("ESCURO", reading.luminosityStatus)
        assertEquals("2026-06-07T01:00:00Z", reading.readAt)
        assertNull(reading.luminosity)
    }

    @Test
    fun keepsLegacyNumericLuminosity() {
        val dto = TelemetryResponseDto(
            id = "reading-1",
            smartPotId = "pot-1",
            soilHumidity = 45,
            temperature = 23.0,
            luminosity = 800.0,
            readAt = "2026-06-06T01:00:00Z",
        )

        assertEquals(800.0, dto.toDomain().luminosity)
    }
}
