package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.home.model.AutomationSummaryUi
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

// ─────────────────────────────────────────────────────────────────────────────
// AutomationSummaryCard.kt
//
// Card de resumo da configuração de automação da planta ativa.
// Exibe as configurações atuais vindas do backend (modo, próxima irrigação,
// limiar de umidade e duração).
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AutomationSummaryCard(
    summary: AutomationSummaryUi,
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
                text = "Automation Summary",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
            )

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = LeafOnColors.BorderDefault)
            Spacer(Modifier.height(16.dp))

            // Cada linha de detalhe é um composable próprio para evitar
            // repetição de código e garantir espaçamento consistente.
            SummaryRow(label = "Mode",               value = summary.mode)
            SummaryRow(label = "Next Watering",      value = summary.nextWatering)
            SummaryRow(
                label = "Humidity Threshold",
                value = "${summary.humidityThreshold}%",
                valueIsBold = true,
            )
            SummaryRow(label = "Duration",           value = "${summary.durationSeconds} seconds")
        }
    }
}

// ── Linha de detalhe reutilizável ─────────────────────────────────────────────

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueIsBold: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (valueIsBold) FontWeight.Bold else FontWeight.Normal,
            color = LeafOnColors.TextPrimary,
        )
    }
}

