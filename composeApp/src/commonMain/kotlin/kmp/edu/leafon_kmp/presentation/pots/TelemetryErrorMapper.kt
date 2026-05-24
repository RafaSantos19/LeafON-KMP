package kmp.edu.leafon_kmp.presentation.pots

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

enum class TelemetryOperation {
    LOAD_LATEST,
    CREATE,
}

object TelemetryErrorMapper {
    fun fromThrowable(
        throwable: Throwable,
        operation: TelemetryOperation,
    ): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 400 -> {
                "Leitura invalida. Verifique os valores enviados."
            }

            throwable is ApiException && throwable.statusCode == 401 -> {
                "Sessao invalida. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode == 403 -> {
                "Voce nao tem permissao para registrar leitura neste vaso."
            }

            throwable is ApiException && throwable.statusCode == 404 -> {
                when (operation) {
                    TelemetryOperation.CREATE -> "Vaso nao encontrado."
                    TelemetryOperation.LOAD_LATEST -> "Nenhuma leitura registrada ainda."
                }
            }

            throwable is UnresolvedAddressException ||
                throwable is SocketTimeoutException ||
                throwable is HttpRequestTimeoutException ||
                "failed to fetch" in message ||
                "network" in message -> {
                "Nao foi possivel conectar a API."
            }

            throwable.message.isNullOrBlank() -> {
                "Ocorreu um erro inesperado."
            }

            else -> throwable.message ?: "Ocorreu um erro inesperado."
        }
    }
}
