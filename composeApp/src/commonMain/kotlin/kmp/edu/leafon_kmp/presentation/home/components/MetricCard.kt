package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.home.model.MetricUi
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

// ─────────────────────────────────────────────────────────────────────────────
// MetricCard.kt
//
// Card de métrica genérico. Exibe um label, um valor numérico em destaque
// e sua unidade de medida.
//
// Ao manter o componente completamente genérico (sem nenhuma referência a
// "temperatura" ou "umidade"), ele pode ser reutilizado para qualquer nova
// métrica que o backend venha a fornecer sem alterar o composable.
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun MetricCard(
    metric: MetricUi,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            // Label (ex: "Temperatura")
            Text(
                text = metric.label,
                fontSize = 14.sp,
                color = LeafOnColors.TextSecondary,
            )

            Spacer(Modifier.height(10.dp))

            // Valor + unidade lado a lado com tamanhos diferentes,
            // criando hierarquia visual como no wireframe.
            Row(verticalAlignment = androidx.compose.ui.Alignment.Bottom) {
                Text(
                    text = metric.value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.TextPrimary,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = metric.unit,
                    fontSize = 16.sp,
                    color = LeafOnColors.TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp), // alinha à linha de base
                )
            }
        }
    }
}

