package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading
import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.CreateTelemetryRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.LatestTelemetryResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.TelemetryResponseDto

fun TelemetryResponseDto.toDomain(
    smartPotIdFallback: String? = null,
): TelemetryReading {
    val resolvedId = id.orEmpty().trim()
    val resolvedSmartPotId = smartPotId.orEmpty().trim()
        .ifBlank { smartPotIdFallback.orEmpty().trim() }
    val resolvedCreatedAt = createdAt?.trim()?.takeIf { it.isNotBlank() }
    val resolvedReadAt = readAt.orEmpty().trim()
        .ifBlank { resolvedCreatedAt.orEmpty() }

    if (
        resolvedSmartPotId.isBlank() ||
        soilHumidity == null ||
        temperature == null
    ) {
        throw ApiException(
            statusCode = -1,
            message = "Resposta da API com dados incompletos de telemetria.",
        )
    }

    return TelemetryReading(
        id = resolvedId.ifBlank {
            "$resolvedSmartPotId-${resolvedReadAt.ifBlank { soilHumidity.toString() }}"
        },
        smartPotId = resolvedSmartPotId,
        soilHumidity = soilHumidity,
        temperature = temperature,
        airHumidity = airHumidity,
        luminosity = luminosity,
        luminosityStatus = luminosityStatus?.trim()?.takeIf { it.isNotBlank() },
        readAt = resolvedReadAt.takeIf { it.isNotBlank() },
        createdAt = resolvedCreatedAt,
    )
}

fun TelemetryReading.toCreateRequest(): CreateTelemetryRequestDto {
    val numericLuminosity = luminosity ?: throw ApiException(
        statusCode = -1,
        message = "Luminosidade numerica obrigatoria para criar telemetria legada.",
    )
    val timestamp = readAt ?: throw ApiException(
        statusCode = -1,
        message = "Data da leitura obrigatoria para criar telemetria legada.",
    )

    return CreateTelemetryRequestDto(
        smartPotId = smartPotId,
        soilHumidity = soilHumidity,
        temperature = temperature,
        luminosity = numericLuminosity,
        readAt = timestamp,
    )
}

fun LatestTelemetryResponseDto.toDomain(smartPotId: String): LatestTelemetryReading {
    return LatestTelemetryReading(
        smartPotId = smartPotId,
        soilHumidity = soilHumidity,
        airHumidity = airHumidity,
        temperature = temperature,
        luminosityStatus = luminosityStatus.trim(),
        readAt = readAt?.trim()?.takeIf { it.isNotBlank() },
    )
}
