package kmp.edu.leafon_kmp.presentation.register

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val plantName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
