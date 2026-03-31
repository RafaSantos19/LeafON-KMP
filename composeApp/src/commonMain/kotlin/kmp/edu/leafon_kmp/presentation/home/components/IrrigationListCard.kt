package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.home.model.IrrigationUi
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

@Composable
fun IrrigationListCard(
    irrigations: List<IrrigationUi>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Recent Irrigations",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
            )

            Spacer(Modifier.padding(top = 6.dp))
            HorizontalDivider(color = LeafOnColors.BorderDefault)
            Spacer(Modifier.padding(top = 8.dp))

            if (irrigations.isEmpty()) {
                Text(
                    text = "No irrigations recorded yet.",
                    fontSize = 13.sp,
                    color = LeafOnColors.TextSecondary,
                )
            } else {
                irrigations.forEach { irrigation ->
                    IrrigationRow(irrigation = irrigation)
                }
            }
        }
    }
}

@Composable
private fun IrrigationRow(irrigation: IrrigationUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(LeafOnColors.Success, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "OK",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.padding(start = 12.dp))
        Text(
            text = irrigation.timestamp,
            fontSize = 14.sp,
            color = LeafOnColors.TextPrimary,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = irrigation.type,
            fontSize = 12.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

