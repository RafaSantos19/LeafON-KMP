package kmp.edu.leafon_kmp.data.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kmp.edu.leafon_kmp.core.auth.AuthErrorMapper
import kmp.edu.leafon_kmp.core.auth.AuthRepository
import kmp.edu.leafon_kmp.core.auth.AuthResult
import kmp.edu.leafon_kmp.core.auth.AuthSession
import kmp.edu.leafon_kmp.core.auth.SupabaseConfig
import kmp.edu.leafon_kmp.core.network.MutableAuthTokenProvider
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseAuthRepository(
    private val supabaseClient: SupabaseClient,
    private val tokenProvider: MutableAuthTokenProvider,
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): AuthResult {
        val configError = SupabaseConfig.configurationErrorOrNull()
        if (configError != null) {
            return AuthResult.Error(configError)
        }

        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val session = supabaseClient.auth.currentSessionOrNull()?.toAuthSession()
                ?: return AuthResult.Error("Token ausente apos o login.")

            tokenProvider.setAccessToken(session.accessToken)
            AuthResult.Success(session)
        } catch (throwable: Throwable) {
            tokenProvider.setAccessToken(null)
            AuthResult.Error(AuthErrorMapper.fromThrowable(throwable))
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String?,
    ): AuthResult {
        val configError = SupabaseConfig.configurationErrorOrNull()
        if (configError != null) {
            return AuthResult.Error(configError)
        }

        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = buildJsonObject {
                    put("name", name)
                    if (!phone.isNullOrBlank()) {
                        put("phone", phone)
                    }
                }
            }

            val session = supabaseClient.auth.currentSessionOrNull()?.toAuthSession()
            if (session == null) {
                tokenProvider.setAccessToken(null)
                AuthResult.EmailConfirmationRequired(email)
            } else {
                tokenProvider.setAccessToken(session.accessToken)
                AuthResult.Success(session)
            }
        } catch (throwable: Throwable) {
            tokenProvider.setAccessToken(null)
            AuthResult.Error(AuthErrorMapper.fromThrowable(throwable))
        }
    }

    override suspend fun signOut() {
        try {
            supabaseClient.auth.signOut()
        } finally {
            tokenProvider.setAccessToken(null)
        }
    }

    override suspend fun getCurrentSession(): AuthSession? {
        return try {
            supabaseClient.auth.currentSessionOrNull()?.toAuthSession()?.also { session ->
                tokenProvider.setAccessToken(session.accessToken)
            } ?: run {
                tokenProvider.setAccessToken(null)
                null
            }
        } catch (_: Throwable) {
            tokenProvider.setAccessToken(null)
            null
        }
    }

    private fun io.github.jan.supabase.auth.user.UserSession.toAuthSession(): AuthSession {
        return AuthSession(
            accessToken = accessToken,
            userId = user?.id,
            email = user?.email,
        )
    }
}
