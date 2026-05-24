package kmp.edu.leafon_kmp.presentation.pots

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

enum class SmartPotOperation {
    LIST,
    DETAIL,
    CREATE,
    UPDATE,
    DELETE,
}

object SmartPotErrorMapper {
    fun fromThrowable(
        throwable: Throwable,
        operation: SmartPotOperation,
    ): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 400 -> {
                when (operation) {
                    SmartPotOperation.CREATE,
                    SmartPotOperation.UPDATE,
                    -> "Verifique os dados informados."

                    else -> "Requisicao invalida para SmartPot."
                }
            }

            throwable is ApiException && throwable.statusCode == 401 -> {
                "Sessao invalida. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode == 403 -> {
                "Voce nao tem permissao para acessar este vaso."
            }

            throwable is ApiException && throwable.statusCode == 404 -> {
                when (operation) {
                    SmartPotOperation.DELETE -> "O vaso nao existe mais."
                    else -> "Vaso nao encontrado."
                }
            }

            throwable is ApiException && throwable.statusCode == 409 -> {
                "Este dispositivo ja esta vinculado a outro vaso."
            }

            throwable is ApiException && throwable.statusCode >= 500 -> {
                "A API LeafON esta indisponivel no momento."
            }

            throwable is UnresolvedAddressException ||
                throwable is SocketTimeoutException ||
                throwable is HttpRequestTimeoutException ||
                "failed to fetch" in message ||
                "network" in message -> {
                "Falha de rede. Verifique sua conexao e a URL da API."
            }

            throwable.message.isNullOrBlank() -> {
                "Ocorreu um erro inesperado."
            }

            else -> throwable.message ?: "Ocorreu um erro inesperado."
        }
    }
}
