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
import kmp.edu.leafon_kmp.core.model.AlertStatus
import kmp.edu.leafon_kmp.core.model.AlertType
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun AlertStatusBadge(
    type: AlertType,
    modifier: Modifier = Modifier,
) {
    val badge = typeBadgeData(type)
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

private fun typeBadgeData(type: AlertType) = when (type) {
    AlertType.LOW_SOIL_HUMIDITY -> BadgeData("Umidade baixa", Color(0xFF8A6D00))
    AlertType.UNKNOWN -> BadgeData("Alerta", LeafOnColors.TextSecondary)
}

private fun statusBadgeData(status: AlertStatus) = when (status) {
    AlertStatus.PENDING -> BadgeData("Pendente", LeafOnColors.Error)
    AlertStatus.READ -> BadgeData("Lido", LeafOnColors.Success)
    AlertStatus.UNKNOWN -> BadgeData("Desconhecido", LeafOnColors.TextSecondary)
}
