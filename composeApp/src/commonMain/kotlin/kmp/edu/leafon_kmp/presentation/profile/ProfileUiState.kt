package kmp.edu.leafon_kmp.presentation.profile

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
)
