package kmp.edu.leafon_kmp.presentation.pots.alerts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertSeverity
import kmp.edu.leafon_kmp.presentation.pots.alerts.model.AlertStatus

@Composable
fun AlertStatusBadge(
    severity: AlertSeverity,
    modifier: Modifier = Modifier,
) {
    val badge = severityBadgeData(severity)
    AlertBadge(
        label = badge.label,
        color = badge.color,
        modifier = modifier,
    )
}

@Composable
fun AlertStatusBadge(
    status: AlertStatus,
    modifier: Modifier = Modifier,
) {
    val badge = statusBadgeData(status)
    AlertBadge(
        label = badge.label,
        color = badge.color,
        modifier = modifier,
    )
}

@Composable
private fun AlertBadge(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(color, CircleShape),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private data class BadgeData(
    val label: String,
    val color: Color,
)

private fun severityBadgeData(severity: AlertSeverity) = when (severity) {
    AlertSeverity.INFO -> BadgeData("Info", LeafOnColors.TextSecondary)
    AlertSeverity.WARNING -> BadgeData("Atencao", Color(0xFF8A6D00))
    AlertSeverity.CRITICAL -> BadgeData("Critico", LeafOnColors.Error)
}

private fun statusBadgeData(status: AlertStatus) = when (status) {
    AlertStatus.ACTIVE -> BadgeData("Ativo", LeafOnColors.Error)
    AlertStatus.RESOLVED -> BadgeData("Resolvido", LeafOnColors.Success)
}
