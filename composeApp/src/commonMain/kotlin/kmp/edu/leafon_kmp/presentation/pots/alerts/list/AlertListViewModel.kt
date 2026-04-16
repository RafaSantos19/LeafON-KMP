package kmp.edu.leafon_kmp.presentation.pots.alerts.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertSeverity
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertStatus
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertUi

class AlertListViewModel(
    private val potId: String,
) {

    var state by mutableStateOf(AlertListState(potId = potId))
        private set

    init {
        loadAlerts()
    }

    fun onAction(action: AlertListAction) {
        when (action) {
            AlertListAction.OnRetryClick -> loadAlerts()
            AlertListAction.OnBackClick -> Unit
        }
    }

    private fun loadAlerts() {
        state = state.copy(
            alerts = getFakeAlerts(potId),
            isLoading = false,
            errorMessage = null,
        )
    }
}

private fun getFakeAlerts(potId: String): List<AlertUi> {
    return when (potId) {
        "1" -> listOf(
            AlertUi(
                id = "$potId-low-humidity",
                title = "Umidade abaixo do ideal",
                description = "O solo esta abaixo da faixa recomendada para esta planta.",
                severity = AlertSeverity.WARNING,
                status = AlertStatus.ACTIVE,
                createdAtLabel = "Hoje, 08:10",
            ),
            AlertUi(
                id = "$potId-routine-ok",
                title = "Irrigacao executada",
                description = "A rotina matinal foi concluida sem falhas.",
                severity = AlertSeverity.INFO,
                status = AlertStatus.RESOLVED,
                createdAtLabel = "Hoje, 08:02",
            ),
        )
        "3" -> listOf(
            AlertUi(
                id = "$potId-critical-dry",
                title = "Risco critico de ressecamento",
                description = "A umidade ficou muito baixa por mais de 30 minutos.",
                severity = AlertSeverity.CRITICAL,
                status = AlertStatus.ACTIVE,
                createdAtLabel = "Hoje, 07:44",
            ),
            AlertUi(
                id = "$potId-high-temp",
                title = "Temperatura elevada",
                description = "A temperatura ambiente esta acima da faixa configurada.",
                severity = AlertSeverity.WARNING,
                status = AlertStatus.ACTIVE,
                createdAtLabel = "Hoje, 07:20",
            ),
            AlertUi(
                id = "$potId-light",
                title = "Luminosidade intensa",
                description = "O pot recebeu luz direta acima do recomendado.",
                severity = AlertSeverity.INFO,
                status = AlertStatus.RESOLVED,
                createdAtLabel = "Ontem, 15:35",
            ),
        )
        "4" -> listOf(
            AlertUi(
                id = "$potId-offline",
                title = "Pot offline",
                description = "Nao foi possivel receber telemetria deste dispositivo.",
                severity = AlertSeverity.CRITICAL,
                status = AlertStatus.ACTIVE,
                createdAtLabel = "Hoje, 06:18",
            ),
        )
        else -> listOf(
            AlertUi(
                id = "$potId-stable",
                title = "Sem eventos criticos",
                description = "As leituras recentes estao dentro da faixa esperada.",
                severity = AlertSeverity.INFO,
                status = AlertStatus.RESOLVED,
                createdAtLabel = "Hoje, 09:00",
            ),
            AlertUi(
                id = "$potId-light-warning",
                title = "Luminosidade reduzida",
                description = "Considere mover o pot para um local com mais luz indireta.",
                severity = AlertSeverity.WARNING,
                status = AlertStatus.ACTIVE,
                createdAtLabel = "Ontem, 17:12",
            ),
        )
    }
}
