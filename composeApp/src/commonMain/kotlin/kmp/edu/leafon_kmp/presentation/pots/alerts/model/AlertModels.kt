package kmp.edu.leafon_kmp.presentation.pots.alerts.model

import androidx.compose.ui.graphics.Color
import kmp.edu.leafon_kmp.core.model.Alert
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.core.model.AlertType
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

fun Alert.typeLabel(): String =
    when (type) {
        AlertType.LOW_SOIL_HUMIDITY -> "Umidade baixa"
        AlertType.UNKNOWN -> "Alerta"
    }

fun Alert.statusLabel(): String =
    when (status) {
        AlertStatus.PENDING -> "Pendente"
        AlertStatus.READ -> "Lido"
        AlertStatus.UNKNOWN -> "Desconhecido"
    }

fun Alert.typeBadgeColor(): Color =
    when (type) {
        AlertType.LOW_SOIL_HUMIDITY -> Color(0xFF8A6D00)
        AlertType.UNKNOWN -> LeafOnColors.TextSecondary
    }

fun Alert.statusBadgeColor(): Color =
    when (status) {
        AlertStatus.PENDING -> LeafOnColors.Error
        AlertStatus.READ -> LeafOnColors.Success
        AlertStatus.UNKNOWN -> LeafOnColors.TextSecondary
    }
