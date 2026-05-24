package kmp.edu.leafon_kmp.core.model

data class Alert(
    val id: String,
    val smartPotId: String,
    val telemetryReadingId: String? = null,
    val type: AlertType,
    val message: String,
    val status: AlertStatus,
    val createdAt: String,
    val readAt: String? = null,
)

enum class AlertType {
    LOW_SOIL_HUMIDITY,
    UNKNOWN,
}

enum class AlertStatus {
    PENDING,
    READ,
    UNKNOWN,
}
