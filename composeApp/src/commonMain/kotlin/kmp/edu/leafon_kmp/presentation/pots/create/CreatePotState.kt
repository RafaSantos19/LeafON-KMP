package kmp.edu.leafon_kmp.presentation.pots.create

data class CreatePotState(
    val name: String = "",
    val plantName: String = "",
    val deviceId: String = "",
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)
