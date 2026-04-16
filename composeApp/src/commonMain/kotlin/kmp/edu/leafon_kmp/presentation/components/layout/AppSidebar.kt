package kmp.edu.leafon_kmp.presentation.components.layout

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import leafon_kmp.composeapp.generated.resources.Res
import leafon_kmp.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

enum class SidebarDestination {
    HOME,
    HISTORY,
    PLANT_AND_POT,
    ALERTS,
    PROFILE
}

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
        SidebarItem(
            destination = SidebarDestination.HOME,
            label = "Home",
            icon = Icons.Outlined.Home,
            onClick = onHomeClick
        ),
        SidebarItem(
            destination = SidebarDestination.HISTORY,
            label = "Historico",
            icon = Icons.Outlined.History,
            onClick = onHistoryClick
        ),
        SidebarItem(
            destination = SidebarDestination.PLANT_AND_POT,
            label = "Planta & Vaso",
            icon = Icons.Outlined.Spa,
            onClick = onPlantAndPotClick
        ),
        SidebarItem(
            destination = SidebarDestination.ALERTS,
            label = "Alertas",
            icon = Icons.Outlined.Notifications,
            onClick = onAlertsClick
        ),
        SidebarItem(
            destination = SidebarDestination.PROFILE,
            label = "Perfil",
            icon = Icons.Outlined.Person,
            onClick = onProfileClick
        ),
    )

    if (compact) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(LeafOnColors.BgMain)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Leaf.ON logo",
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            items.forEach { item ->
                SidebarNavChip(
                    item = item,
                    isSelected = selectedDestination == item.destination
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .width(240.dp)
                .fillMaxHeight()
                .background(LeafOnColors.BgMain)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            SidebarHeader()

            items.forEach { item ->
                SidebarNavItem(
                    item = item,
                    isSelected = selectedDestination == item.destination
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            SidebarFooter()
        }
    }
}

private data class SidebarItem(
    val destination: SidebarDestination,
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
private fun SidebarHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = LeafOnColors.BgSoftGreen,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Leaf.ON logo",
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "LeafON",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.GreenPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Painel principal",
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary
        )
    }
}

@Composable
private fun SidebarNavItem(
    item: SidebarItem,
    isSelected: Boolean,
) {
    val containerColor = if (isSelected) {
        LeafOnColors.BgSoftGreen
    } else {
        Color.Transparent
    }

    val iconContainerColor = if (isSelected) {
        LeafOnColors.GreenPrimary.copy(alpha = 0.12f)
    } else {
        LeafOnColors.BgSecondary
    }

    val contentColor = if (isSelected) {
        LeafOnColors.GreenPrimary
    } else {
        LeafOnColors.TextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(14.dp))
            .clickable { item.onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(iconContainerColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.label,
            fontSize = 15.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
private fun SidebarNavChip(
    item: SidebarItem,
    isSelected: Boolean,
) {
    val bgColor = if (isSelected) {
        LeafOnColors.GreenPrimary
    } else {
        LeafOnColors.BgSecondary
    }

    val contentColor = if (isSelected) {
        LeafOnColors.TextOnDark
    } else {
        LeafOnColors.TextSecondary
    }

    Row(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(999.dp))
            .clickable { item.onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}

@Composable
private fun SidebarFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(LeafOnColors.BgSecondary)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "LeafON v1.0",
            fontSize = 12.sp,
            color = LeafOnColors.TextSecondary
        )
    }
}
