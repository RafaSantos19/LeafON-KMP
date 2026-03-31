package kmp.edu.leafon_kmp.presentation.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

enum class SidebarDestination { HOME, HISTORY, PLANT_AND_POT, ALERTS, PROFILE }

@Composable
fun AppSidebar(
    selectedDestination: SidebarDestination = SidebarDestination.HOME,
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onPlantAndPotClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        SidebarItem(SidebarDestination.HOME, "Home", "H", onHomeClick),
        SidebarItem(SidebarDestination.HISTORY, "History", "R", onHistoryClick),
        SidebarItem(SidebarDestination.PLANT_AND_POT, "Plant & Pot", "P", onPlantAndPotClick),
        SidebarItem(SidebarDestination.ALERTS, "Alerts", "A", onAlertsClick),
        SidebarItem(SidebarDestination.PROFILE, "Profile", "U", onProfileClick),
    )

    if (compact) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(LeafOnColors.BgMain)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                SidebarNavChip(
                    item = item,
                    isSelected = selectedDestination == item.destination,
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .width(220.dp)
                .fillMaxHeight()
                .background(LeafOnColors.BgMain)
                .padding(vertical = 16.dp, horizontal = 12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, bottom = 28.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(LeafOnColors.BgSoftGreen, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "L",
                        color = LeafOnColors.GreenPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "LeafON",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = LeafOnColors.GreenPrimary,
                )
            }

            items.forEach { item ->
                SidebarNavItem(
                    item = item,
                    isSelected = selectedDestination == item.destination,
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

private data class SidebarItem(
    val destination: SidebarDestination,
    val label: String,
    val marker: String,
    val onClick: () -> Unit,
)

@Composable
private fun SidebarNavItem(
    item: SidebarItem,
    isSelected: Boolean,
) {
    val bgColor = if (isSelected) LeafOnColors.BgSoftGreen else LeafOnColors.BgMain
    val textColor = if (isSelected) LeafOnColors.GreenPrimary else LeafOnColors.TextSecondary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(10.dp))
            .clickable { item.onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(textColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = item.marker,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = item.label,
            fontSize = 15.sp,
            fontWeight = fontWeight,
            color = textColor,
        )
    }
}

@Composable
private fun SidebarNavChip(
    item: SidebarItem,
    isSelected: Boolean,
) {
    val bgColor = if (isSelected) LeafOnColors.GreenPrimary else LeafOnColors.BgSecondary
    val textColor = if (isSelected) LeafOnColors.TextOnDark else LeafOnColors.TextSecondary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(999.dp))
            .clickable { item.onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Text(
            text = item.marker,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = item.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
        )
    }
}
