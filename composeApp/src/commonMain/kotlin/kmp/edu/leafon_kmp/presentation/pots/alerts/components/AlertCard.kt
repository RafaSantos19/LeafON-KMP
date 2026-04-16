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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertSeverity
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertStatus
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertUi

@Composable
fun AlertCard(
    alert: AlertUi,
    modifier: Modifier = Modifier,
) {
    val isCriticalActive = alert.severity == AlertSeverity.CRITICAL &&
        alert.status == AlertStatus.ACTIVE

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCriticalActive) 3.dp else 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isCriticalActive) LeafOnColors.Error.copy(alpha = 0.35f) else LeafOnColors.BorderDefault,
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
                severity = alert.severity,
                active = alert.status == AlertStatus.ACTIVE,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AlertCardHeader(alert = alert)

                Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LeafOnColors.TextSecondary,
                    lineHeight = 20.sp,
                )

                AlertTimestamp(createdAtLabel = alert.createdAtLabel)
            }
        }
    }
}

@Composable
private fun AlertCardHeader(alert: AlertUi) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = alert.title,
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
            AlertStatusBadge(severity = alert.severity)
            AlertStatusBadge(status = alert.status)
        }
    }
}

@Composable
private fun AlertIcon(
    severity: AlertSeverity,
    active: Boolean,
) {
    val iconColor = severityColor(severity)
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
private fun AlertTimestamp(createdAtLabel: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = LeafOnColors.TextSecondary,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = createdAtLabel,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

private fun severityColor(severity: AlertSeverity): Color {
    return when (severity) {
        AlertSeverity.INFO -> LeafOnColors.TextSecondary
        AlertSeverity.WARNING -> Color(0xFF8A6D00)
        AlertSeverity.CRITICAL -> LeafOnColors.Error
    }
}
