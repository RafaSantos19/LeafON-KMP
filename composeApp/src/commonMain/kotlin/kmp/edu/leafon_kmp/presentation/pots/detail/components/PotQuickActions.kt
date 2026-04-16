package kmp.edu.leafon_kmp.presentation.pots.detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
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
    onViewRoutinesClick: () -> Unit,
    onViewAlertsClick: () -> Unit,
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
                        modifier = Modifier.weight(1f),
                    )
                    RoutinesActionButton(
                        onClick = onViewRoutinesClick,
                        modifier = Modifier.weight(1f),
                    )
                    AlertsActionButton(
                        onClick = onViewAlertsClick,
                        modifier = Modifier.weight(1f),
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    EditActionButton(onClick = onEditClick)
                    RoutinesActionButton(onClick = onViewRoutinesClick)
                    AlertsActionButton(onClick = onViewAlertsClick)
                }
            }
        }
    }
}

@Composable
private fun EditActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
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
private fun RoutinesActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
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
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
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
