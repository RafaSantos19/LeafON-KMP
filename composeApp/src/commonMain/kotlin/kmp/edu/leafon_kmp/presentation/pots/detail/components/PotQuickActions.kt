package kmp.edu.leafon_kmp.presentation.pots.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun PotQuickActions(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onGenerateTelemetryClick: () -> Unit,
    onViewRoutinesClick: () -> Unit,
    onViewAlertsClick: () -> Unit,
    isDeleting: Boolean,
    isSendingTelemetry: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Acoes rapidas",
            style = MaterialTheme.typography.titleMedium,
            color = LeafOnColors.TextPrimary,
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            if (maxWidth >= 720.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    EditActionButton(
                        onClick = onEditClick,
                        enabled = !isDeleting,
                        modifier = Modifier.weight(1f),
                    )
                    DeleteActionButton(
                        onClick = onDeleteClick,
                        enabled = !isDeleting,
                        modifier = Modifier.weight(1f),
                    )
                    GenerateTelemetryActionButton(
                        onClick = onGenerateTelemetryClick,
                        enabled = !isDeleting && !isSendingTelemetry,
                        isSendingTelemetry = isSendingTelemetry,
                        modifier = Modifier.weight(1f),
                    )
                    RoutinesActionButton(
                        onClick = onViewRoutinesClick,
                        enabled = !isDeleting,
                        modifier = Modifier.weight(1f),
                    )
                    AlertsActionButton(
                        onClick = onViewAlertsClick,
                        enabled = !isDeleting,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    EditActionButton(onClick = onEditClick, enabled = !isDeleting)
                    DeleteActionButton(onClick = onDeleteClick, enabled = !isDeleting)
                    GenerateTelemetryActionButton(
                        onClick = onGenerateTelemetryClick,
                        enabled = !isDeleting && !isSendingTelemetry,
                        isSendingTelemetry = isSendingTelemetry,
                    )
                    RoutinesActionButton(onClick = onViewRoutinesClick, enabled = !isDeleting)
                    AlertsActionButton(onClick = onViewAlertsClick, enabled = !isDeleting)
                }
            }
        }
    }
}

@Composable
private fun EditActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LeafOnColors.GreenPrimary,
            contentColor = LeafOnColors.TextOnDark,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
        )
        Text("Editar pot")
    }
}

@Composable
private fun DeleteActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LeafOnColors.Error,
            contentColor = LeafOnColors.TextOnDark,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = null,
        )
        Text(if (enabled) "Excluir vaso" else "Excluindo...")
    }
}

@Composable
private fun GenerateTelemetryActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isSendingTelemetry: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LeafOnColors.Warning,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Science,
            contentDescription = null,
        )
        Text(if (isSendingTelemetry) "Enviando leitura..." else "Gerar leitura de teste")
    }
}

@Composable
private fun RoutinesActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LeafOnColors.GreenPrimary,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Repeat,
            contentDescription = null,
        )
        Text("Ver rotinas")
    }
}

@Composable
private fun AlertsActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 46.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = LeafOnColors.Error,
        ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
        )
        Text("Ver alertas")
    }
}
