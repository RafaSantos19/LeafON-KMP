package kmp.edu.leafon_kmp.presentation.pots.routines.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.routines.components.RoutineFormContent

@Composable
fun CreateRoutineScreen(
    potId: String,
    onBackClick: () -> Unit,
    onRoutineCreated: () -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId, onRoutineCreated) {
        CreateRoutineViewModel(
            potId = potId,
            onCreated = onRoutineCreated,
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactCreateRoutineLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        } else {
            ExpandedCreateRoutineLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
                onNotificationsClick = onNotificationsClick,
            )
        }
    }
}

@Composable
private fun ExpandedCreateRoutineLayout(
    state: CreateRoutineState,
    onAction: (CreateRoutineAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LeafOnColors.BorderDefault),
        )

        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                state = createRoutineTopBarState(),
                onNotificationsClick = onNotificationsClick,
                onProfileClick = onProfileClick,
            )

            CreateRoutineContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactCreateRoutineLayout(
    state: CreateRoutineState,
    onAction: (CreateRoutineAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = createRoutineTopBarState(),
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        AppSidebar(
            selectedDestination = SidebarDestination.PLANT_AND_POT,
            onHomeClick = onHomeClick,
            onPlantAndPotClick = onPotsClick,
            onAlertsClick = onAlertsClick,
            onProfileClick = onProfileClick,
            compact = true,
        )

        CreateRoutineContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CreateRoutineContent(
    state: CreateRoutineState,
    onAction: (CreateRoutineAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        RoutineFormContent(
            name = state.name,
            time = state.time,
            selectedDays = state.selectedDays,
            durationSec = state.durationSec,
            enabled = state.enabled,
            isSaving = state.isSaving,
            errorMessage = state.errorMessage,
            onNameChange = { value ->
                onAction(CreateRoutineAction.OnNameChange(value))
            },
            onTimeChange = { value ->
                onAction(CreateRoutineAction.OnTimeChange(value))
            },
            onToggleDay = { day ->
                onAction(CreateRoutineAction.OnToggleDay(day))
            },
            onDurationChange = { value ->
                onAction(CreateRoutineAction.OnDurationChange(value))
            },
            onToggleEnabled = {
                onAction(CreateRoutineAction.OnToggleEnabled)
            },
            onSubmitClick = {
                onAction(CreateRoutineAction.OnSaveClick)
            },
            onCancelClick = onBackClick,
        )
    }
}

private fun createRoutineTopBarState() = AppTopBarState(
    title = "Nova rotina",
    subject = "Automacao de irrigacao",
    subjectOnline = true,
    lastUpdateLabel = "Defina horario, dias e duracao",
)
