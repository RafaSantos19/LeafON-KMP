package kmp.edu.leafon_kmp.presentation.pots.routines.components

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
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

@Composable
fun RoutineCard(
    routine: RoutineUi,
    onToggleClick: () -> Unit,
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
            RoutineIcon(enabled = routine.enabled)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = routine.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                RoutineScheduleInfo(routine = routine)
            }

            Switch(
                checked = routine.enabled,
                onCheckedChange = { onToggleClick() },
            )
        }
    }
}

@Composable
private fun RoutineIcon(enabled: Boolean) {
    val iconColor = if (enabled) LeafOnColors.GreenPrimary else LeafOnColors.TextSecondary
    val containerColor = if (enabled) LeafOnColors.BgSoftGreen else LeafOnColors.BgSecondary

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(containerColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.WaterDrop,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(23.dp),
        )
    }
}

@Composable
private fun RoutineScheduleInfo(routine: RoutineUi) {
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
                text = "${routine.timeLabel} - ${routine.durationSec}s",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        Text(
            text = routine.daysLabel,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
