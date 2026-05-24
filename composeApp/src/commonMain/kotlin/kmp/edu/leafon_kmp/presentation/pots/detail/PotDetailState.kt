package kmp.edu.leafon_kmp.presentation.pots.detail

import kmp.edu.leafon_kmp.core.model.TelemetryReading

data class PotDetailState(
    val potId: String,
    val plantName: String = "",
    val humidityMin: Int? = null,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isLoading: Boolean = true,
    val isTelemetryLoading: Boolean = false,
    val isSendingTelemetry: Boolean = false,
    val isDeleting: Boolean = false,
    val latestTelemetry: TelemetryReading? = null,
    val errorMessage: String? = null,
    val telemetryErrorMessage: String? = null,
    val feedbackMessage: String? = null,
)
