package kmp.edu.leafon_kmp.presentation.pots

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

enum class AlertOperation {
    LIST,
    MARK_AS_READ,
}

object AlertErrorMapper {
    fun fromThrowable(
        throwable: Throwable,
        operation: AlertOperation,
    ): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 400 -> {
                "Requisicao invalida."
            }

            throwable is ApiException && throwable.statusCode == 401 -> {
                "Sessao invalida. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode == 403 -> {
                "Voce nao tem permissao para acessar este alerta."
            }

            throwable is ApiException && throwable.statusCode == 404 -> {
                "Alerta nao encontrado."
            }

            throwable is ApiException && throwable.statusCode >= 500 -> {
                if (operation == AlertOperation.MARK_AS_READ) {
                    "Nao foi possivel atualizar o alerta."
                } else {
                    "Nao foi possivel carregar os alertas."
                }
            }

            throwable is UnresolvedAddressException ||
                throwable is SocketTimeoutException ||
                throwable is HttpRequestTimeoutException ||
                "failed to fetch" in message ||
                "network" in message -> {
                "Nao foi possivel conectar a API."
            }

            operation == AlertOperation.MARK_AS_READ -> {
                "Nao foi possivel atualizar o alerta."
            }

            else -> "Nao foi possivel carregar os alertas."
        }
    }
}
