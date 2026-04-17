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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
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
    repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
    onHomeClick: () -> Unit = onBackClick,
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(repositorio, onPotCreated) {
        CreatePotViewModel(
            repositorio = repositorio,
            onCreated = onPotCreated,
        )
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
            name = state.name,
            plantName = state.plantName,
            deviceId = state.deviceId,
            isSaving = state.isSaving,
            errorMessage = state.errorMessage,
            onNameChange = { value ->
                onAction(CreatePotAction.OnNameChange(value))
            },
            onPlantNameChange = { value ->
                onAction(CreatePotAction.OnPlantNameChange(value))
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
    title = "Cadastrar pot",
    subject = "Novo Smart Pot",
    subjectOnline = false,
    lastUpdateLabel = "Preencha os dados iniciais",
)
