package kmp.edu.leafon_kmp.core.model

data class TelemetryReading(
    val id: String,
    val smartPotId: String,
    val soilHumidity: Int,
    val temperature: Double,
    val luminosity: Double,
    val readAt: String,
    val createdAt: String?,
)
