package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.CreateSmartPotRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.SmartPotResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.UpdateSmartPotRequestDto

fun SmartPotResponseDto.toDomain(): SmartPot {
    val resolvedId = id.orEmpty().trim()

    if (resolvedId.isBlank()) {
        throw ApiException(
            statusCode = -1,
            message = "Resposta da API sem id para SmartPot.",
        )
    }

    return SmartPot(
        id = resolvedId,
        plantName = plantName.trim(),
        humidityMin = humidityMin,
        deviceId = deviceId?.trim()?.takeIf { it.isNotBlank() },
        createdAt = createdAt?.trim()?.takeIf { it.isNotBlank() },
        updatedAt = updatedAt?.trim()?.takeIf { it.isNotBlank() },
    )
}

fun SmartPot.toCreateRequest(): CreateSmartPotRequestDto {
    return CreateSmartPotRequestDto(
        plantName = plantName.trim(),
        humidityMin = humidityMin,
        deviceId = deviceId?.trim()?.takeIf { it.isNotBlank() },
    )
}

fun SmartPot.toUpdateRequest(): UpdateSmartPotRequestDto {
    return UpdateSmartPotRequestDto(
        plantName = plantName.trim(),
        humidityMin = humidityMin,
        deviceId = deviceId?.trim()?.takeIf { it.isNotBlank() },
    )
}
