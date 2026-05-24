package kmp.edu.leafon_kmp.core.model

data class SmartPot(
    val id: String,
    val plantName: String,
    val humidityMin: Int,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
