package kmp.edu.leafon_kmp.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination

@Composable
fun ProfileRouteScreen(
    viewModel: ProfileViewModel,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    onLoggedOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactProfileLayout(
                viewModel = viewModel,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onLoggedOut = onLoggedOut,
            )
        } else {
            ExpandedProfileLayout(
                viewModel = viewModel,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onLoggedOut = onLoggedOut,
            )
        }
    }
}

@Composable
private fun ExpandedProfileLayout(
    viewModel: ProfileViewModel,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.PROFILE,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = {},
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LeafOnColors.BorderDefault),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                state = profileTopBarState(),
                onProfileClick = {},
            )

            ProfileScreen(
                viewModel = viewModel,
                onLoggedOut = onLoggedOut,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactProfileLayout(
    viewModel: ProfileViewModel,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onLoggedOut: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = profileTopBarState(),
            onProfileClick = {},
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.PROFILE,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = {},
            compact = true,
        )

        ProfileScreen(
            viewModel = viewModel,
            onLoggedOut = onLoggedOut,
            modifier = Modifier.weight(1f),
        )
    }
}

private fun profileTopBarState() = AppTopBarState(
    title = "Perfil",
    subject = "Dados da conta",
    subjectOnline = true,
    lastUpdateLabel = "Mantenha seus dados atualizados",
)
