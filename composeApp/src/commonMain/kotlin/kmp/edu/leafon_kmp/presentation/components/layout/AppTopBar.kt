package kmp.edu.leafon_kmp.presentation.components.layout

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors

data class AppTopBarState(
    val title: String = "",
    val subject: String = "",
    val subjectOnline: Boolean = false,
    val lastUpdateLabel: String = "",
)

@Composable
fun AppTopBar(
    state: AppTopBarState,
    onNotificationsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LeafOnColors.BgMain),
    ) {
        if (compact) {
            CompactTopBar(
                state = state,
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )
        } else {
            ExpandedTopBar(
                state = state,
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )
        }

        HorizontalDivider(color = LeafOnColors.BorderDefault, thickness = 1.dp)
    }
}

@Composable
private fun ExpandedTopBar(
    state: AppTopBarState,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = state.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )

        Spacer(Modifier.width(28.dp))
        Text(
            text = state.subject,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
        )

        Spacer(Modifier.width(12.dp))
        OnlineBadge(isOnline = state.subjectOnline)
        Spacer(Modifier.width(12.dp))

        Text(
            text = state.lastUpdateLabel,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )

        Spacer(Modifier.weight(1f))
        ActionChip(label = "N", onClick = onNotificationsClick)
        Spacer(Modifier.width(8.dp))
        ProfileAvatar(onProfileClick = onProfileClick)
    }
}

@Composable
private fun CompactTopBar(
    state: AppTopBarState,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LeafOnColors.TextPrimary,
            )
            Spacer(Modifier.weight(1f))
            ActionChip(label = "N", onClick = onNotificationsClick)
            Spacer(Modifier.width(8.dp))
            ProfileAvatar(onProfileClick = onProfileClick)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = state.subject,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            OnlineBadge(isOnline = state.subjectOnline)
        }

        Text(
            text = state.lastUpdateLabel,
            fontSize = 13.sp,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
private fun ActionChip(
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(LeafOnColors.BgSecondary, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextPrimary,
        )
    }
}

@Composable
private fun ProfileAvatar(
    onProfileClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(LeafOnColors.BgSecondary)
            .clickable { onProfileClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "U",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = LeafOnColors.TextSecondary,
        )
    }
}

@Composable
fun OnlineBadge(isOnline: Boolean) {
    val bgColor = if (isOnline) LeafOnColors.GreenHover else LeafOnColors.TextSecondary
    val label = if (isOnline) "Online" else "Offline"

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            color = LeafOnColors.TextOnDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
