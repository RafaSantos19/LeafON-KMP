package kmp.edu.leafon_kmp.presentation.pots.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi

@Composable
fun PotCard(
    pot: PotUi,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            PotCardHeader(pot = pot)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = LeafOnColors.BorderDefault)
            Spacer(Modifier.height(16.dp))
            PotCardMetrics(pot = pot)
            Spacer(Modifier.height(14.dp))
            PotCardFooter(
                lastUpdate = pot.lastUpdateLabel,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        }
    }
}

@Composable
private fun PotCardHeader(pot: PotUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        PotAvatar()
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pot.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = LeafOnColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = pot.plantType,
                fontSize = 12.sp,
                color = LeafOnColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        StatusBadge(status = pot.status)
    }
}

@Composable
private fun PotAvatar() {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(LeafOnColors.BgSoftGreen),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Spa,
            contentDescription = null,
            tint = LeafOnColors.GreenPrimary,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun StatusBadge(status: PotStatus) {
    val (statusColor, label) = when (status) {
        PotStatus.ONLINE -> LeafOnColors.Success to "Online"
        PotStatus.OFFLINE -> LeafOnColors.TextSecondary to "Offline"
        PotStatus.ATTENTION -> LeafOnColors.Warning to "Atencao"
    }

    Row(
        modifier = Modifier
            .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(statusColor, CircleShape),
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = statusColor,
        )
    }
}

@Composable
private fun PotCardMetrics(pot: PotUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MetricChip(
            icon = Icons.Outlined.WaterDrop,
            label = "Umidade",
            value = pot.humidityPercent?.let { "$it%" } ?: "-",
            tint = LeafOnColors.GreenPrimary,
            highlight = true,
            modifier = Modifier.weight(1f),
        )
        MetricChip(
            icon = Icons.Outlined.Thermostat,
            label = "Temperatura",
            value = pot.temperatureCelsius?.let { "${it}C" } ?: "-",
            tint = LeafOnColors.TextSecondary,
            highlight = false,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MetricChip(
    icon: ImageVector,
    label: String,
    value: String,
    tint: Color,
    highlight: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (highlight) {
        LeafOnColors.BgSoftGreen
    } else {
        LeafOnColors.BgSecondary
    }

    Row(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = LeafOnColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (highlight) LeafOnColors.GreenPrimary else LeafOnColors.TextPrimary,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun PotCardFooter(
    lastUpdate: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = LeafOnColors.TextSecondary,
                modifier = Modifier.size(13.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Ultima atualizacao: $lastUpdate",
                fontSize = 11.sp,
                color = LeafOnColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        PotCardActions(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
private fun PotCardActions(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        IconButton(
            onClick = onEditClick,
            modifier = Modifier.size(34.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Editar pot",
                tint = LeafOnColors.TextSecondary,
                modifier = Modifier.size(18.dp),
            )
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(34.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Excluir pot",
                tint = LeafOnColors.Error,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
