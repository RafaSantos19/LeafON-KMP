package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

enum class ChartRange(val label: String) {
    H24("24h"),
    D7("7d"),
    D15("15d"),
    D30("30d"),
}

@Composable
fun HumidityChartCard(
    selectedRange: ChartRange = ChartRange.H24,
    onRangeSelected: (ChartRange) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Humidity History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                RangeSelector(
                    selectedRange = selectedRange,
                    onRangeSelected = onRangeSelected,
                )
            }

            Spacer(Modifier.height(20.dp))
            ChartPlaceholder()
        }
    }
}

@Composable
private fun RangeSelector(
    selectedRange: ChartRange,
    onRangeSelected: (ChartRange) -> Unit,
) {
    Row(
        modifier = Modifier
            .background(LeafOnColors.BgSecondary, RoundedCornerShape(8.dp))
            .padding(4.dp),
    ) {
        ChartRange.entries.forEach { range ->
            RangeButton(
                label = range.label,
                isSelected = range == selectedRange,
                onClick = { onRangeSelected(range) },
            )
        }
    }
}

@Composable
private fun RangeButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected) LeafOnColors.TextPrimary else LeafOnColors.BgSecondary
    val textColor = if (isSelected) LeafOnColors.TextOnDark else LeafOnColors.TextSecondary

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = textColor,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ChartPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(LeafOnColors.BgSoftGreen, RoundedCornerShape(12.dp))
            .border(1.dp, LeafOnColors.BorderDefault, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Chart goes here",
                fontSize = 14.sp,
                color = LeafOnColors.TextSecondary,
            )
            Text(
                text = "Integrate Vico or Canvas-based chart",
                fontSize = 12.sp,
                color = LeafOnColors.TextSecondary.copy(alpha = 0.6f),
            )
        }
    }
}

