package kmp.edu.leafon_kmp.core.auth

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

object AuthErrorMapper {
    fun fromThrowable(throwable: Throwable): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 401 -> {
                "Token invalido ou expirado. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode >= 500 -> {
                "A API LeafON esta indisponivel no momento."
            }

            throwable is UnresolvedAddressException || throwable is SocketTimeoutException -> {
                "Falha de rede. Verifique sua conexao e a URL da API."
            }

            "invalid login credentials" in message || "invalid_credentials" in message -> {
                "Email ou senha invalidos."
            }

            "email not confirmed" in message || "email_not_confirmed" in message -> {
                "Confirme seu email antes de entrar."
            }

            "user already registered" in message || "already been registered" in message -> {
                "Ja existe uma conta cadastrada com este email."
            }

            "jwt" in message && "invalid" in message -> {
                "Token ausente ou invalido."
            }

            "failed to fetch" in message || "network" in message -> {
                "Falha de rede. Verifique sua conexao e tente novamente."
            }

            throwable.message.isNullOrBlank() -> {
                "Ocorreu um erro inesperado."
            }

            else -> throwable.message ?: "Ocorreu um erro inesperado."
        }
    }
}
