package kmp.edu.leafon_kmp.presentation.home

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

object HomeErrorMapper {
    fun fromThrowable(throwable: Throwable): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 401 -> {
                "Sessao invalida. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode == 403 -> {
                "Voce nao tem permissao para acessar estes dados."
            }

            throwable is UnresolvedAddressException ||
                throwable is SocketTimeoutException ||
                throwable is HttpRequestTimeoutException ||
                "failed to fetch" in message ||
                "network" in message -> {
                "Nao foi possivel conectar a API."
            }

            else -> "Nao foi possivel carregar o dashboard."
        }
    }
}
