package kmp.edu.leafon_kmp.presentation.pots.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi

@Composable
fun PotGrid(
    pots: List<PotUi>,
    columns: Int,
    onPotClick: (String) -> Unit,
    onEditPotClick: (String) -> Unit,
    onDeletePotClick: (String) -> Unit,
    onAddPotClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = pots.map { PotGridItem.ExistingPot(it) } + PotGridItem.AddNew
    val rows = items.chunked(columns)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        rows.forEach { rowItems ->
            PotGridRow(
                items = rowItems,
                columns = columns,
                onPotClick = onPotClick,
                onEditPotClick = onEditPotClick,
                onDeletePotClick = onDeletePotClick,
                onAddPotClick = onAddPotClick,
            )
        }
    }
}

@Composable
private fun PotGridRow(
    items: List<PotGridItem>,
    columns: Int,
    onPotClick: (String) -> Unit,
    onEditPotClick: (String) -> Unit,
    onDeletePotClick: (String) -> Unit,
    onAddPotClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items.forEach { item ->
            Box(modifier = Modifier.weight(1f)) {
                when (item) {
                    is PotGridItem.ExistingPot -> PotCard(
                        pot = item.pot,
                        onClick = { onPotClick(item.pot.id) },
                        onEditClick = { onEditPotClick(item.pot.id) },
                        onDeleteClick = { onDeletePotClick(item.pot.id) },
                    )
                    is PotGridItem.AddNew -> AddPotCard(onClick = onAddPotClick)
                }
            }
        }

        repeat(columns - items.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private sealed interface PotGridItem {
    data class ExistingPot(val pot: PotUi) : PotGridItem
    data object AddNew : PotGridItem
}
