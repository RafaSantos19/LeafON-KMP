package kmp.edu.leafon_kmp.presentation.pots.detail.components

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
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

@Composable
fun PotStatusBadge(
    status: PotStatus,
    modifier: Modifier = Modifier,
) {
    val badge = statusBadgeData(status)

    Row(
        modifier = modifier
            .background(badge.color.copy(alpha = 0.14f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(badge.color, CircleShape),
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = badge.label,
            color = badge.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private data class StatusBadgeData(
    val label: String,
    val color: Color,
)

private fun statusBadgeData(status: PotStatus) = when (status) {
    PotStatus.ONLINE -> StatusBadgeData("Online", LeafOnColors.Success)
    PotStatus.OFFLINE -> StatusBadgeData("Offline", LeafOnColors.TextSecondary)
    PotStatus.ATTENTION -> StatusBadgeData("Atencao", LeafOnColors.Warning)
}
