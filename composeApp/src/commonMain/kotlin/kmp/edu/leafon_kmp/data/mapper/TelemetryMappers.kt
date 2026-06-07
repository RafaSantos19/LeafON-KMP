package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.CreateTelemetryRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.TelemetryResponseDto

fun TelemetryResponseDto.toDomain(): TelemetryReading {
    val resolvedId = id.orEmpty().trim()
    val resolvedSmartPotId = smartPotId.orEmpty().trim()
    val resolvedReadAt = readAt.orEmpty().trim()

    if (
        resolvedId.isBlank() ||
        resolvedSmartPotId.isBlank() ||
        soilHumidity == null ||
        temperature == null ||
        luminosity == null ||
        resolvedReadAt.isBlank()
    ) {
        throw ApiException(
            statusCode = -1,
            message = "Resposta da API com dados incompletos de telemetria.",
        )
    }

    return TelemetryReading(
        id = resolvedId,
        smartPotId = resolvedSmartPotId,
        soilHumidity = soilHumidity,
        temperature = temperature,
        luminosity = luminosity,
        readAt = resolvedReadAt,
        createdAt = createdAt?.trim()?.takeIf { it.isNotBlank() },
    )
}

fun TelemetryReading.toCreateRequest(): CreateTelemetryRequestDto {
    return CreateTelemetryRequestDto(
        smartPotId = smartPotId,
        soilHumidity = soilHumidity,
        temperature = temperature,
        luminosity = luminosity,
        readAt = readAt,
    )
}
