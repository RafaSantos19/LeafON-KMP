package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.home.model.AlertLevel
import kmp.edu.leafon_kmp.presentation.home.model.AlertUi
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

// ─────────────────────────────────────────────────────────────────────────────
// AlertListCard.kt
//
// Card de alertas recentes. A cor do ícone circular muda de acordo com o
// [AlertLevel] do item, dando ao usuário um sinal visual rápido de severidade
// sem precisar ler o texto completo.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AlertListCard(
    alerts: List<AlertUi>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            Text(
                text = "Recent Alerts",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = LeafOnColors.BorderDefault)
            Spacer(Modifier.height(8.dp))

            if (alerts.isEmpty()) {
                Text(
                    text = "No alerts. Your plant is happy! 🌿",
                    fontSize = 13.sp,
                    color = LeafOnColors.TextSecondary,
                )
            } else {
                alerts.forEach { alert ->
                    AlertRow(alert = alert)
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}

// ── Linha individual de alerta ────────────────────────────────────────────────

@Composable
private fun AlertRow(alert: AlertUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        // Ícone circular colorido conforme a severidade
        AlertLevelIndicator(level = alert.level)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = alert.message,
                fontSize = 14.sp,
                color = LeafOnColors.TextPrimary,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = alert.timestamp,
                fontSize = 12.sp,
                color = LeafOnColors.TextSecondary,
            )
        }
    }
}

// ── Indicador de nível ────────────────────────────────────────────────────────

@Composable
private fun AlertLevelIndicator(level: AlertLevel) {
    val color = when (level) {
        AlertLevel.CRITICAL -> LeafOnColors.Error
        AlertLevel.WARNING  -> LeafOnColors.Warning
        AlertLevel.INFO     -> LeafOnColors.GreenHover
    }
    // Inicial da severidade como label acessível dentro do círculo
    val label = when (level) {
        AlertLevel.CRITICAL -> "C"
        AlertLevel.WARNING  -> "W"
        AlertLevel.INFO     -> "I"
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = LeafOnColors.TextOnDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

