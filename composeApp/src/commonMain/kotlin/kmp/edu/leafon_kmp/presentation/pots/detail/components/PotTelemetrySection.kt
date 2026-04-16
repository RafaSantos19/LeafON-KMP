package kmp.edu.leafon_kmp.presentation.pots.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun PotTelemetrySection(
    humidityPercent: Int?,
    temperatureCelsius: Int?,
    lightLevel: Int?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Metricas principais",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isWide = maxWidth >= 720.dp
            val metricItems = listOf(
                TelemetryItem(
                    icon = Icons.Outlined.WaterDrop,
                    label = "Umidade",
                    value = humidityPercent?.let { "$it%" } ?: "-",
                    helper = "Solo monitorado",
                    tint = LeafOnColors.GreenPrimary,
                ),
                TelemetryItem(
                    icon = Icons.Outlined.Thermostat,
                    label = "Temperatura",
                    value = temperatureCelsius?.let { "${it}C" } ?: "-",
                    helper = "Ambiente atual",
                    tint = LeafOnColors.TextPrimary,
                ),
                TelemetryItem(
                    icon = Icons.Outlined.LightMode,
                    label = "Luminosidade",
                    value = lightLevel?.let { "$it%" } ?: "-",
                    helper = "Nivel de luz",
                    tint = LeafOnColors.Warning,
                ),
            )

            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    metricItems.forEach { item ->
                        TelemetryCard(
                            item = item,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    metricItems.forEach { item ->
                        TelemetryCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryCard(
    item: TelemetryItem,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            IconBox(
                icon = item.icon,
                tint = item.tint,
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.label,
                    fontSize = 13.sp,
                    color = LeafOnColors.TextSecondary,
                )
                Text(
                    text = item.value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.TextPrimary,
                )
                Text(
                    text = item.helper,
                    fontSize = 12.sp,
                    color = LeafOnColors.TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun IconBox(
    icon: ImageVector,
    tint: Color,
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(42.dp)
            .background(tint.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
    }
}

private data class TelemetryItem(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val helper: String,
    val tint: Color,
)
