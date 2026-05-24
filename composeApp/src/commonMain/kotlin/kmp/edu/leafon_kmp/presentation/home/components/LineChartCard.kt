package kmp.edu.leafon_kmp.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

enum class ChartRange(
    val label: String,
    val pointLimit: Int,
) {
    H24("24h", 8),
    D7("7 dias", 14),
    D15("15 dias", 20),
    D30("30 dias", 30),
}

@Composable
fun ChartRangeSelector(
    selectedRange: ChartRange,
    onRangeSelected: (ChartRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(LeafOnColors.BgMain, RoundedCornerShape(10.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ChartRange.entries.forEach { range ->
            if (range == selectedRange) {
                Button(
                    onClick = { onRangeSelected(range) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.TextPrimary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                ) {
                    Text(range.label)
                }
            } else {
                OutlinedButton(onClick = { onRangeSelected(range) }) {
                    Text(range.label)
                }
            }
        }
    }
}

@Composable
fun LineChartCard(
    title: String,
    values: List<Float>,
    valueSuffix: String = "",
    emptyMessage: String = "Nenhuma leitura registrada para este vaso.",
    color: Color = LeafOnColors.GreenPrimary,
    modifier: Modifier = Modifier,
) {
    val latestValue = values.lastOrNull()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LeafOnColors.BgMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LeafOnColors.TextPrimary,
                )
                Text(
                    text = latestValue?.let { "${formatChartValue(it)}$valueSuffix" } ?: "--",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.TextPrimary,
                )
            }

            if (values.isEmpty()) {
                EmptyChartState(message = emptyMessage)
            } else {
                SimpleLineChart(
                    values = values,
                    color = color,
                )
            }
        }
    }
}

@Composable
private fun SimpleLineChart(
    values: List<Float>,
    color: Color,
) {
    val normalizedValues = normalizeValues(values)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(LeafOnColors.BgSoftGreen, RoundedCornerShape(12.dp))
            .border(1.dp, LeafOnColors.BorderDefault, RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(156.dp)) {
            val width = size.width
            val height = size.height

            if (normalizedValues.size == 1) {
                drawCircle(
                    color = color,
                    radius = 6.dp.toPx(),
                    center = Offset(width / 2f, height / 2f),
                )
                return@Canvas
            }

            val horizontalStep = width / normalizedValues.lastIndex.coerceAtLeast(1)
            val points = normalizedValues.mapIndexed { index, value ->
                Offset(
                    x = horizontalStep * index,
                    y = height - (value * height),
                )
            }

            val path = Path().apply {
                points.forEachIndexed { index, point ->
                    if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                ),
            )

            points.forEach { point ->
                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = point,
                )
            }
        }
    }
}

@Composable
private fun EmptyChartState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(LeafOnColors.BgSoftGreen, RoundedCornerShape(12.dp))
            .border(1.dp, LeafOnColors.BorderDefault, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            fontSize = 14.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

private fun normalizeValues(values: List<Float>): List<Float> {
    if (values.isEmpty()) return emptyList()

    val minValue = values.minOrNull() ?: return emptyList()
    val maxValue = values.maxOrNull() ?: return emptyList()

    if (minValue == maxValue) {
        return values.map { 0.5f }
    }

    return values.map { value ->
        ((value - minValue) / (maxValue - minValue)).coerceIn(0f, 1f)
    }
}

private fun formatChartValue(value: Float): String {
    return if (value % 1f == 0f) {
        value.toInt().toString()
    } else {
        "%.1f".format(value)
    }
}
