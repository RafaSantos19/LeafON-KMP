package kmp.edu.leafon_kmp.core.model

data class TelemetryReading(
    val id: String,
    val smartPotId: String,
    val soilHumidity: Int,
    val temperature: Double,
    val airHumidity: Double? = null,
    val luminosity: Double? = null,
    val luminosityStatus: String? = null,
    val readAt: String? = null,
    val createdAt: String?,
)

data class LatestTelemetryReading(
    val smartPotId: String,
    val soilHumidity: Int,
    val airHumidity: Double,
    val temperature: Double,
    val luminosityStatus: String,
    val readAt: String? = null,
)
