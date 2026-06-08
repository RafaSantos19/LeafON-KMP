package kmp.edu.leafon_kmp.data.remote.dto

import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryReading
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothTelemetryDtoTest {

    @Test
    fun syncRequestContainsOnlyBackendFields() {
        val request = BluetoothTelemetryReading(
            soilHumidity = 92,
            soilHumidityRaw = 352,
            airHumidity = 68.4,
            temperature = 21.4,
            luminosityStatus = "CLARO",
            luminosityDigital = 0,
            receivedAt = "2026-06-06T22:00:00Z",
        ).toSyncRequest()

        val fields = Json.parseToJsonElement(
            Json.encodeToString(request)
        ).jsonObject.keys

        assertEquals(
            setOf(
                "soilHumidity",
                "soilHumidityRaw",
                "airHumidity",
                "temperature",
                "luminosityStatus",
                "luminosityDigital",
            ),
            fields,
        )
    }
}
