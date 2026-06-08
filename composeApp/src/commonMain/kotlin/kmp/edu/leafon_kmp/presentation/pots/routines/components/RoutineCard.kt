package kmp.edu.leafon_kmp.presentation.pots.routines.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.core.model.Routine
import kmp.edu.leafon_kmp.core.model.RoutineType
import kmp.edu.leafon_kmp.core.time.ReadableTimestampFormatter
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.pots.routines.model.label
import kmp.edu.leafon_kmp.presentation.pots.routines.model.statusLabel
import kmp.edu.leafon_kmp.presentation.pots.routines.model.toDaysLabel

@Composable
fun RoutineCard(
    routine: Routine,
    isBusy: Boolean,
    onEditClick: () -> Unit,
    onToggleActiveClick: () -> Unit,
    onSimulateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (routine.active) LeafOnColors.GreenPrimary.copy(alpha = 0.25f) else LeafOnColors.BorderDefault,
        ),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 420.dp
            val cardPadding = if (compact) 14.dp else 18.dp

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(cardPadding),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    RoutineIcon(type = routine.type, active = routine.active)

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = routine.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = LeafOnColors.TextPrimary,
                            maxLines = if (compact) 2 else 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        RoutineBadges(
                            routine = routine,
                            compact = compact,
                        )
                    }
                }

                RoutineScheduleInfo(routine = routine)
                RoutineExecutionInfo(routine = routine)

                Text(
                    text = "Simular execucao nao aciona hardware fisico.",
                    fontSize = 12.sp,
                    color = LeafOnColors.TextSecondary,
                )

                RoutineCardActions(
                    routine = routine,
                    isBusy = isBusy,
                    compact = compact,
                    onEditClick = onEditClick,
                    onToggleActiveClick = onToggleActiveClick,
                    onSimulateClick = onSimulateClick,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }
}

@Composable
private fun RoutineBadges(
    routine: Routine,
    compact: Boolean,
) {
    val typeColor = if (routine.type == RoutineType.LIGHTING) Color(0xFF8A6D00) else LeafOnColors.GreenPrimary
    val statusColor = if (routine.active) LeafOnColors.Success else LeafOnColors.TextSecondary

    if (compact) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RoutineBadge(label = routine.type.label(), color = typeColor)
            RoutineBadge(label = routine.statusLabel(), color = statusColor)
        }
    } else {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RoutineBadge(label = routine.type.label(), color = typeColor)
            RoutineBadge(label = routine.statusLabel(), color = statusColor)
        }
    }
}

@Composable
private fun RoutineCardActions(
    routine: Routine,
    isBusy: Boolean,
    compact: Boolean,
    onEditClick: () -> Unit,
    onToggleActiveClick: () -> Unit,
    onSimulateClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    if (compact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = onEditClick,
                enabled = !isBusy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Editar")
            }

            Button(
                onClick = onToggleActiveClick,
                enabled = !isBusy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (routine.active) LeafOnColors.TextSecondary else LeafOnColors.GreenPrimary,
                    contentColor = LeafOnColors.TextOnDark,
                ),
            ) {
                Icon(
                    imageVector = if (routine.active) Icons.Outlined.ToggleOff else Icons.Outlined.ToggleOn,
                    contentDescription = null,
                )
                Spacer(Modifier.width(6.dp))
                Text(if (routine.active) "Desativar" else "Ativar")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onSimulateClick,
                    enabled = !isBusy,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                ) {
                    Icon(Icons.Outlined.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Simular")
                }

                OutlinedButton(
                    onClick = onDeleteClick,
                    enabled = !isBusy,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LeafOnColors.Error,
                    ),
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Excluir")
                }
            }
        }
        return
    }

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        OutlinedButton(
            onClick = onEditClick,
            enabled = !isBusy,
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Editar")
        }

        Button(
            onClick = onToggleActiveClick,
            enabled = !isBusy,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (routine.active) LeafOnColors.TextSecondary else LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            Icon(
                imageVector = if (routine.active) Icons.Outlined.ToggleOff else Icons.Outlined.ToggleOn,
                contentDescription = null,
            )
            Spacer(Modifier.width(6.dp))
            Text(if (routine.active) "Desativar" else "Ativar")
        }

        OutlinedButton(
            onClick = onSimulateClick,
            enabled = !isBusy,
        ) {
            Icon(Icons.Outlined.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Simular")
        }

        OutlinedButton(
            onClick = onDeleteClick,
            enabled = !isBusy,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LeafOnColors.Error,
            ),
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Excluir")
        }
    }
}

@Composable
private fun RoutineIcon(
    type: RoutineType,
    active: Boolean,
) {
    val iconColor = when {
        !active -> LeafOnColors.TextSecondary
        type == RoutineType.LIGHTING -> Color(0xFF8A6D00)
        else -> LeafOnColors.GreenPrimary
    }
    val containerColor = if (active) LeafOnColors.BgSoftGreen else LeafOnColors.BgSecondary

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(containerColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (type == RoutineType.LIGHTING) {
                Icons.Outlined.LightMode
            } else {
                Icons.Outlined.WaterDrop
            },
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(23.dp),
        )
    }
}

@Composable
private fun RoutineBadge(
    label: String,
    color: Color,
) {
    Row(
        modifier = Modifier
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

@Composable
private fun RoutineScheduleInfo(routine: Routine) {
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
                text = "${routine.scheduledTime} - ${routine.durationSec}s",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        Text(
            text = routine.daysOfWeek.toDaysLabel(),
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RoutineExecutionInfo(routine: Routine) {
    val lastExecuted = ReadableTimestampFormatter.formatOrFallback(routine.lastExecutedAt)
    val updatedAt = ReadableTimestampFormatter.formatOrFallback(routine.updatedAt ?: routine.createdAt)

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (!lastExecuted.isNullOrBlank()) {
            Text(
                text = "Ultima simulacao: $lastExecuted",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }

        if (!updatedAt.isNullOrBlank()) {
            Text(
                text = "Atualizado em: $updatedAt",
                fontSize = 13.sp,
                color = LeafOnColors.TextSecondary,
            )
        }
    }
}
