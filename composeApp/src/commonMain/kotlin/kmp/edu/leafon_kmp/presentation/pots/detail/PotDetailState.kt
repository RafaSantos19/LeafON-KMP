package kmp.edu.leafon_kmp.presentation.pots.detail

data class PotDetailState(
    val potId: String,
    val plantName: String = "",
    val humidityMin: Int? = null,
    val deviceId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
)
