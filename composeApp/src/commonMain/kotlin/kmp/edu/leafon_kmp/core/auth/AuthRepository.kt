package kmp.edu.leafon_kmp.core.auth

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String?,
    ): AuthResult

    suspend fun signOut()

    suspend fun getCurrentSession(): AuthSession?
}
