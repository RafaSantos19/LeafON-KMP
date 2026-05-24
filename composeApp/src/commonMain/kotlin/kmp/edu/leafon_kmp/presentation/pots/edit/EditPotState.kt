package kmp.edu.leafon_kmp.presentation.pots.edit

data class EditPotState(
    val potId: String = "",
    val plantName: String = "",
    val humidityMin: String = "",
    val deviceId: String = "",
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)
