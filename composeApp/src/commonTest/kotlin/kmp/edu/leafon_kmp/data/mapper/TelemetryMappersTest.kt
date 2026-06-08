package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.TelemetryResponseDto
import kotlin.test.Test
import kotlin.test.assertFailsWith

class TelemetryMappersTest {

    @Test
    fun partialTelemetryResponseBecomesControlledApiError() {
        val response = TelemetryResponseDto(
            id = "reading-1",
            smartPotId = "pot-1",
            soilHumidity = 42,
        )

        assertFailsWith<ApiException> {
            response.toDomain()
        }
    }
}
