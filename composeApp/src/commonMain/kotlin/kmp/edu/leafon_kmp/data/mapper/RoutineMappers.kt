package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.core.model.RoutineType
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.CreateRoutineRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.RoutineResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.UpdateRoutineRequestDto

fun RoutineResponseDto.toDomain(): Routine {
    val resolvedId = id.orEmpty().trim()
    val resolvedSmartPotId = smartPotId.trim()

    if (resolvedId.isBlank() || resolvedSmartPotId.isBlank()) {
        throw ApiException(
            statusCode = -1,
            message = "Resposta da API sem identificadores validos para rotina.",
        )
    }

    return Routine(
        id = resolvedId,
        smartPotId = resolvedSmartPotId,
        type = type.toRoutineType(),
        name = name.trim(),
        scheduledTime = scheduledTime.trim(),
        daysOfWeek = daysOfWeek.trim(),
        durationSec = durationSec,
        active = active,
        lastExecutedAt = lastExecutedAt?.trim()?.takeIf { it.isNotBlank() },
        createdAt = createdAt?.trim()?.takeIf { it.isNotBlank() },
        updatedAt = updatedAt?.trim()?.takeIf { it.isNotBlank() },
    )
}

fun Routine.toCreateRequest(): CreateRoutineRequestDto {
    return CreateRoutineRequestDto(
        smartPotId = smartPotId.trim(),
        type = type.toApiValue(),
        name = name.trim(),
        scheduledTime = scheduledTime.trim(),
        daysOfWeek = daysOfWeek.trim(),
        durationSec = durationSec,
        active = active,
    )
}

fun Routine.toUpdateRequest(): UpdateRoutineRequestDto {
    return UpdateRoutineRequestDto(
        smartPotId = smartPotId.trim(),
        type = type.toApiValue(),
        name = name.trim(),
        scheduledTime = scheduledTime.trim(),
        daysOfWeek = daysOfWeek.trim(),
        durationSec = durationSec,
        active = active,
    )
}

private fun String.toRoutineType(): RoutineType =
    when (trim()) {
        "IRRIGATION" -> RoutineType.IRRIGATION
        "LIGHTING" -> RoutineType.LIGHTING
        else -> RoutineType.UNKNOWN
    }

private fun RoutineType.toApiValue(): String =
    when (this) {
        RoutineType.IRRIGATION -> "IRRIGATION"
        RoutineType.LIGHTING -> "LIGHTING"
        RoutineType.UNKNOWN -> "IRRIGATION"
    }
