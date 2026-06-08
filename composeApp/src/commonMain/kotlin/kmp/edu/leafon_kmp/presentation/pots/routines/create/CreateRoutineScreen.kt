package kmp.edu.leafon_kmp.presentation.pots.routines.create

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.data.repository.RoutineRepository
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.routines.components.RoutineFormContent

@Composable
fun CreateRoutineScreen(
    potId: String,
    routineId: String? = null,
    routineRepository: RoutineRepository,
    onBackClick: () -> Unit,
    onRoutineSaved: () -> Unit,
    onHomeClick: () -> Unit = {},
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId, routineId, routineRepository, onRoutineSaved) {
        CreateRoutineViewModel(
            potId = potId,
            routineId = routineId,
            routineRepository = routineRepository,
            onSaved = onRoutineSaved,
        )
    }

    DisposableEffect(viewModel) {
        onDispose { viewModel.onCleared() }
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
                state = createRoutineTopBarState(state.isEditMode),
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
            state = createRoutineTopBarState(state.isEditMode),
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
    when {
        state.isLoading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
        }

        state.errorMessage != null && state.isEditMode && state.name.isBlank() -> RoutineFormErrorState(
            message = state.errorMessage,
            onRetryClick = {
                onAction(CreateRoutineAction.OnRetryLoad)
            },
            onBackClick = onBackClick,
            modifier = modifier,
        )

        else -> BoxWithConstraints(modifier = modifier.fillMaxSize()) {
            val compact = maxWidth < 420.dp

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = if (compact) 16.dp else 24.dp,
                        vertical = if (compact) 18.dp else 28.dp,
                    ),
                contentAlignment = Alignment.TopCenter,
            ) {
                RoutineFormContent(
                    type = state.type,
                    name = state.name,
                    time = state.time,
                    selectedDays = state.selectedDays,
                    durationInput = state.durationInput,
                    active = state.active,
                    isLoading = state.isLoading,
                    isSaving = state.isSaving,
                    errorMessage = state.errorMessage,
                    onTypeChange = { value ->
                        onAction(CreateRoutineAction.OnTypeChange(value))
                    },
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
                    onToggleActive = {
                        onAction(CreateRoutineAction.OnToggleEnabled)
                    },
                    onSubmitClick = {
                        onAction(CreateRoutineAction.OnSaveClick)
                    },
                    onCancelClick = onBackClick,
                    title = if (state.isEditMode) "Editar rotina" else "Nova rotina",
                    subtitle = if (state.isEditMode) {
                        "Atualize configuracoes logicas da rotina sem acionar hardware fisico."
                    } else {
                        "Crie uma rotina logica associada a este Smart Pot."
                    },
                    submitLabel = if (state.isEditMode) "Salvar alteracoes" else "Salvar rotina",
                )
            }
        }
    }
}

@Composable
private fun RoutineFormErrorState(
    message: String,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Nao foi possivel carregar a rotina.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = LeafOnColors.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = message,
                color = LeafOnColors.TextSecondary,
                textAlign = TextAlign.Center,
            )
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.TextSecondary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                ) {
                    Text("Voltar")
                }
                Button(
                    onClick = onRetryClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LeafOnColors.GreenPrimary,
                        contentColor = LeafOnColors.TextOnDark,
                    ),
                    modifier = Modifier.padding(start = 12.dp),
                ) {
                    Text("Tentar novamente")
                }
            }
        }
    }
}

private fun createRoutineTopBarState(isEditMode: Boolean) = AppTopBarState(
    title = if (isEditMode) "Editar rotina" else "Nova rotina",
    subject = "Automacao logica",
    subjectOnline = true,
    lastUpdateLabel = "Defina horario, dias, duracao e status",
)
