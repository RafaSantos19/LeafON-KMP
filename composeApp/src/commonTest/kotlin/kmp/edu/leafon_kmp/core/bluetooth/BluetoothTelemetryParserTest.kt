package kmp.edu.leafon_kmp.core.bluetooth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BluetoothTelemetryParserTest {

    @Test
    fun parsesValidArduinoLine() {
        val reading = parseBluetoothTelemetryReading(
            line = """
                {
                  "soilHumidity": 92,
                  "soilHumidityRaw": 352,
                  "airHumidity": 68.4,
                  "temperature": 21.4,
                  "luminosityStatus": "CLARO",
                  "luminosityDigital": 0
                }
            """.trimIndent(),
            receivedAt = "2026-06-06T22:00:00Z",
        )

        assertEquals(92, reading?.soilHumidity)
        assertEquals("CLARO", reading?.luminosityStatus)
        assertEquals("2026-06-06T22:00:00Z", reading?.receivedAt)
    }

    @Test
    fun ignoresInvalidArduinoLine() {
        assertNull(
            parseBluetoothTelemetryReading(
                line = """{"soilHumidity": 92}""",
                receivedAt = "2026-06-06T22:00:00Z",
            )
        )
    }
}
