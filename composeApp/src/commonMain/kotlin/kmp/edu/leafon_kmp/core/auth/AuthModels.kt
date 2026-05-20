package kmp.edu.leafon_kmp.core.auth

data class AuthSession(
    val accessToken: String,
    val userId: String? = null,
    val email: String? = null,
)

sealed interface AuthResult {
    data class Success(val session: AuthSession) : AuthResult

    data class EmailConfirmationRequired(val email: String) : AuthResult

    data class Error(val message: String) : AuthResult
}
