package kmp.edu.leafon_kmp.data.mapper

import kmp.edu.leafon_kmp.core.model.Alert
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.core.model.AlertType
import kmp.edu.leafon_kmp.core.network.ApiException
import kmp.edu.leafon_kmp.data.remote.dto.AlertResponseDto

fun AlertResponseDto.toDomain(): Alert {
    val resolvedId = id.orEmpty().trim()
    val resolvedSmartPotId = smartPotId.trim()

    if (resolvedId.isBlank() || resolvedSmartPotId.isBlank()) {
        throw ApiException(
            statusCode = -1,
            message = "Resposta da API sem identificadores validos para alerta.",
        )
    }

    return Alert(
        id = resolvedId,
        smartPotId = resolvedSmartPotId,
        telemetryReadingId = telemetryReadingId?.trim()?.takeIf { it.isNotBlank() },
        type = type.toAlertType(),
        message = message.trim(),
        status = status.toAlertStatus(),
        createdAt = createdAt.trim(),
        readAt = readAt?.trim()?.takeIf { it.isNotBlank() },
    )
}

private fun String.toAlertType(): AlertType =
    when (trim()) {
        "LOW_SOIL_HUMIDITY" -> AlertType.LOW_SOIL_HUMIDITY
        else -> AlertType.UNKNOWN
    }

private fun String.toAlertStatus(): AlertStatus =
    when (trim()) {
        "PENDING" -> AlertStatus.PENDING
        "READ" -> AlertStatus.READ
        else -> AlertStatus.UNKNOWN
    }
