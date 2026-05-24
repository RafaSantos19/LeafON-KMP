package kmp.edu.leafon_kmp.presentation.pots

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import kmp.edu.leafon_kmp.core.network.ApiException

enum class RoutineOperation {
    LIST,
    DETAIL,
    CREATE,
    UPDATE,
    ACTIVATE,
    DEACTIVATE,
    SIMULATE,
    DELETE,
}

object RoutineErrorMapper {
    fun fromThrowable(
        throwable: Throwable,
        operation: RoutineOperation,
    ): String {
        val message = throwable.message.orEmpty().lowercase()

        return when {
            throwable is ApiException && throwable.statusCode == 400 -> {
                "Dados invalidos. Verifique os campos."
            }

            throwable is ApiException && throwable.statusCode == 401 -> {
                "Sessao invalida. Faca login novamente."
            }

            throwable is ApiException && throwable.statusCode == 403 -> {
                "Voce nao tem permissao para acessar esta rotina."
            }

            throwable is ApiException && throwable.statusCode == 404 -> {
                "Rotina nao encontrada."
            }

            throwable is UnresolvedAddressException ||
                throwable is SocketTimeoutException ||
                throwable is HttpRequestTimeoutException ||
                "failed to fetch" in message ||
                "network" in message -> {
                "Nao foi possivel conectar a API."
            }

            operation == RoutineOperation.CREATE -> "Nao foi possivel criar a rotina."
            operation == RoutineOperation.UPDATE -> "Nao foi possivel atualizar a rotina."
            operation == RoutineOperation.ACTIVATE -> "Nao foi possivel ativar a rotina."
            operation == RoutineOperation.DEACTIVATE -> "Nao foi possivel desativar a rotina."
            operation == RoutineOperation.SIMULATE -> "Nao foi possivel simular a execucao."
            operation == RoutineOperation.DELETE -> "Nao foi possivel excluir a rotina."
            else -> "Nao foi possivel carregar as rotinas."
        }
    }
}
