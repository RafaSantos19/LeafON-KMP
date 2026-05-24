package kmp.edu.leafon_kmp.presentation.pots.create

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory
import kmp.edu.leafon_kmp.presentation.components.global.LeafOnColors
import kmp.edu.leafon_kmp.presentation.components.layout.AppSidebar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBar
import kmp.edu.leafon_kmp.presentation.components.layout.AppTopBarState
import kmp.edu.leafon_kmp.presentation.components.layout.SidebarDestination
import kmp.edu.leafon_kmp.presentation.pots.components.PotFormContent

@Composable
fun CreatePotScreen(
    onBackClick: () -> Unit,
    onPotCreated: () -> Unit,
    smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
    onHomeClick: () -> Unit = onBackClick,
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(smartPotRepository, onPotCreated) {
        CreatePotViewModel(
            smartPotRepository = smartPotRepository,
            onCreated = onPotCreated,
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
            CompactCreatePotLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
            )
        } else {
            ExpandedCreatePotLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
            )
        }
    }
}

@Composable
private fun ExpandedCreatePotLayout(
    state: CreatePotState,
    onAction: (CreatePotAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
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
                state = createPotTopBarState(),
                onProfileClick = onProfileClick,
            )

            CreatePotContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactCreatePotLayout(
    state: CreatePotState,
    onAction: (CreatePotAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = createPotTopBarState(),
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

        CreatePotContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun CreatePotContent(
    state: CreatePotState,
    onAction: (CreatePotAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        PotFormContent(
            plantName = state.plantName,
            humidityMin = state.humidityMin,
            deviceId = state.deviceId,
            isSaving = state.isSaving,
            errorMessage = state.errorMessage,
            onPlantNameChange = { value ->
                onAction(CreatePotAction.OnPlantNameChange(value))
            },
            onHumidityMinChange = { value ->
                onAction(CreatePotAction.OnHumidityMinChange(value))
            },
            onDeviceIdChange = { value ->
                onAction(CreatePotAction.OnDeviceIdChange(value))
            },
            onSubmitClick = {
                onAction(CreatePotAction.OnSaveClick)
            },
            onCancelClick = onBackClick,
        )
    }
}

private fun createPotTopBarState() = AppTopBarState(
    title = "Cadastrar vaso",
    subject = "Novo Smart Pot",
    subjectOnline = false,
    lastUpdateLabel = "Preencha os dados iniciais",
)
