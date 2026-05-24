package kmp.edu.leafon_kmp.presentation.pots.alerts.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.model.Alert
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.core.time.ReadableTimestampFormatter
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.typeLabel

@Composable
fun AlertCard(
    alert: Alert,
    onMarkAsReadClick: (String) -> Unit,
    isUpdating: Boolean,
    modifier: Modifier = Modifier,
) {
    val isPending = alert.status == AlertStatus.PENDING

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPending) 3.dp else 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isPending) LeafOnColors.Error.copy(alpha = 0.35f) else LeafOnColors.BorderDefault,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            AlertIcon(
                status = alert.status,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AlertCardHeader(alert = alert)

                Text(
                    text = alert.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LeafOnColors.TextSecondary,
                    lineHeight = 20.sp,
                )

                AlertTimestamp(
                    createdAt = ReadableTimestampFormatter.formatOrFallback(alert.createdAt).orEmpty(),
                    readAt = ReadableTimestampFormatter.formatOrFallback(alert.readAt),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isPending) {
                        Button(
                            onClick = {
                                onMarkAsReadClick(alert.id)
                            },
                            enabled = !isUpdating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LeafOnColors.GreenPrimary,
                                contentColor = LeafOnColors.TextOnDark,
                            ),
                        ) {
                            Text(if (isUpdating) "Atualizando..." else "Marcar como lido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertCardHeader(alert: Alert) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = alert.typeLabel(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AlertStatusBadge(type = alert.type)
            AlertStatusBadge(status = alert.status)
        }
    }
}

@Composable
private fun AlertIcon(
    status: AlertStatus,
) {
    val active = status == AlertStatus.PENDING
    val iconColor = if (active) LeafOnColors.Error else LeafOnColors.Success
    val containerColor = if (active) {
        iconColor.copy(alpha = 0.12f)
    } else {
        LeafOnColors.BgSecondary
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(containerColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            tint = if (active) iconColor else LeafOnColors.TextSecondary,
            modifier = Modifier.size(23.dp),
        )
    }
}

@Composable
private fun AlertTimestamp(
    createdAt: String,
    readAt: String?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = LeafOnColors.TextSecondary,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "Criado em $createdAt",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        if (!readAt.isNullOrBlank()) {
            Text(
                text = "Lido em $readAt",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }
    }
}
