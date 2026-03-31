package kmp.edu.leafon_kmp.presentation.profile

// ─────────────────────────────────────────────────────────────────────────────
// ProfileUiState.kt
// ─────────────────────────────────────────────────────────────────────────────

data class ProfileUiState(
    // ── Informações pessoais ──────────────────────────────────────────────────
    val firstName: String = "",
    val lastName: String  = "",
    val email: String     = "",
    val phone: String     = "",
    val gender: String    = "Male",

    // ── Senha ─────────────────────────────────────────────────────────────────
    val currentPassword: String  = "",
    val newPassword: String      = "",
    val confirmPassword: String  = "",

    // ── Controle de UI ────────────────────────────────────────────────────────
    val isSaving: Boolean        = false,
    val hasUnsavedChanges: Boolean = false,
    val saveSuccess: Boolean     = false,
    val errorMessage: String?    = null,
) {
    /** True quando newPassword e confirmPassword são iguais e não-vazios. */
    val passwordsMatch: Boolean
        get() = newPassword.isNotEmpty() && newPassword == confirmPassword

    /** True quando newPassword tem pelo menos 8 caracteres. */
    val newPasswordValid: Boolean
        get() = newPassword.length >= 8

    /** True quando currentPassword foi preenchido. */
    val currentPasswordFilled: Boolean
        get() = currentPassword.isNotEmpty()

    /** Nome completo montado para exibição no ProfileCard. */
    val fullName: String
        get() = "$firstName $lastName".trim()
}
