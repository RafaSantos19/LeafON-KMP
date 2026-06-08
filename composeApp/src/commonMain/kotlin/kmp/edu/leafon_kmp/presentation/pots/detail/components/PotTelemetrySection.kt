package kmp.edu.leafon_kmp.presentation.pots.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.format.format
import kmp.edu.leafon_kmp.core.model.LatestTelemetryReading
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun PotTelemetrySection(
    latestTelemetry: LatestTelemetryReading?,
    isTelemetryLoading: Boolean,
    telemetryErrorMessage: String?,
    humidityMin: Int?,
    deviceId: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Ultima telemetria",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )

        if (telemetryErrorMessage != null && latestTelemetry == null) {
            TelemetryInfoCard(
                message = telemetryErrorMessage,
                tint = LeafOnColors.Warning,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TelemetryCard(
                item = TelemetryItem(
                    icon = Icons.Outlined.WaterDrop,
                    label = "Umidade do solo",
                    value = latestTelemetry?.soilHumidity?.let { "$it%" } ?: "-",
                    helper = if (latestTelemetry != null) {
                        "Minimo configurado: ${humidityMin?.let { "$it%" } ?: "-"}"
                    } else {
                        "Nenhuma leitura registrada ainda."
                    },
                    tint = LeafOnColors.GreenPrimary,
                ),
            )
            TelemetryCard(
                item = TelemetryItem(
                    icon = Icons.Outlined.Thermostat,
                    label = "Temperatura",
                    value = latestTelemetry?.temperature?.let { "${it.format(1)} C" } ?: "-",
                    helper = "Ultima leitura de temperatura",
                    tint = LeafOnColors.TextPrimary,
                ),
            )
            TelemetryCard(
                item = TelemetryItem(
                    icon = Icons.Outlined.WaterDrop,
                    label = "Umidade do ar",
                    value = latestTelemetry?.airHumidity?.let { "${it.format(1)}%" } ?: "-",
                    helper = "Umidade relativa do ambiente",
                    tint = LeafOnColors.TextPrimary,
                ),
            )
            TelemetryCard(
                item = TelemetryItem(
                    icon = Icons.Outlined.LightMode,
                    label = "Luminosidade",
                    value = latestTelemetry?.luminosityStatus ?: "-",
                    helper = "Status informado pelo sensor",
                    tint = LeafOnColors.Warning,
                ),
            )
            TelemetryCard(
                item = TelemetryItem(
                    icon = Icons.Outlined.Dns,
                    label = "Device ID",
                    value = deviceId ?: "-",
                    helper = if (isTelemetryLoading) {
                        "Carregando leitura mais recente..."
                    } else {
                        "Vinculo atual do dispositivo"
                    },
                    tint = LeafOnColors.TextPrimary,
                ),
            )

            when {
                isTelemetryLoading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            color = LeafOnColors.GreenPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = "Carregando ultima leitura...",
                            fontSize = 13.sp,
                            color = LeafOnColors.TextSecondary,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                    }
                }

                latestTelemetry == null && telemetryErrorMessage == null -> {
                    TelemetryInfoCard(
                        message = "Nenhuma leitura registrada ainda.",
                        tint = LeafOnColors.TextSecondary,
                    )
                }

                telemetryErrorMessage != null && latestTelemetry != null -> {
                    TelemetryInfoCard(
                        message = telemetryErrorMessage,
                        tint = LeafOnColors.Warning,
                    )
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
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

@Composable
private fun TelemetryInfoCard(
    message: String,
    tint: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = tint.copy(alpha = 0.1f),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = message,
                fontSize = 13.sp,
                color = LeafOnColors.TextPrimary,
            )
        }
    }
}
