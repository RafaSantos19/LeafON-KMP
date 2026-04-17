package kmp.edu.leafon_kmp.presentation.pots.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun EditPotScreen(
    potId: String,
    onBackClick: () -> Unit,
    onPotUpdated: () -> Unit,
    repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
    onHomeClick: () -> Unit = onBackClick,
    onPotsClick: () -> Unit = onBackClick,
    onAlertsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val viewModel = remember(potId, repositorio, onPotUpdated) {
        EditPotViewModel(
            potId = potId,
            repositorio = repositorio,
            onUpdated = onPotUpdated,
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(LeafOnColors.BgSecondary),
    ) {
        val isCompact = maxWidth < 960.dp

        if (isCompact) {
            CompactEditPotLayout(
                state = viewModel.state,
                onAction = viewModel::onAction,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onPotsClick = onPotsClick,
                onAlertsClick = onAlertsClick,
                onProfileClick = onProfileClick,
            )
        } else {
            ExpandedEditPotLayout(
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
private fun ExpandedEditPotLayout(
    state: EditPotState,
    onAction: (EditPotAction) -> Unit,
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
                state = editPotTopBarState(),
                onProfileClick = onProfileClick,
            )

            EditPotContent(
                state = state,
                onAction = onAction,
                onBackClick = onBackClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CompactEditPotLayout(
    state: EditPotState,
    onAction: (EditPotAction) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPotsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            state = editPotTopBarState(),
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

        EditPotContent(
            state = state,
            onAction = onAction,
            onBackClick = onBackClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun EditPotContent(
    state: EditPotState,
    onAction: (EditPotAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        when {
            state.isLoading -> EditPotLoadingState()
            state.errorMessage != null && state.name.isBlank() -> EditPotErrorState(
                message = state.errorMessage,
                onBackClick = onBackClick,
            )
            else -> PotFormContent(
                name = state.name,
                plantName = state.plantName,
                deviceId = state.deviceId,
                isSaving = state.isSaving,
                errorMessage = state.errorMessage,
                onNameChange = { value ->
                    onAction(EditPotAction.OnNameChange(value))
                },
                onPlantNameChange = { value ->
                    onAction(EditPotAction.OnPlantNameChange(value))
                },
                onDeviceIdChange = { value ->
                    onAction(EditPotAction.OnDeviceIdChange(value))
                },
                onSubmitClick = {
                    onAction(EditPotAction.OnSaveClick)
                },
                onCancelClick = onBackClick,
                title = "Editar pot",
                subtitle = "Atualize as informacoes do vaso selecionado.",
                submitLabel = "Salvar alteracoes",
            )
        }
    }
}

@Composable
private fun EditPotLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = LeafOnColors.GreenPrimary)
    }
}

@Composable
private fun EditPotErrorState(
    message: String,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Nao foi possivel carregar o pot.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = LeafOnColors.TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = LeafOnColors.TextSecondary,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = LeafOnColors.GreenPrimary,
                contentColor = LeafOnColors.TextOnDark,
            ),
        ) {
            Text("Voltar")
        }
    }
}

private fun editPotTopBarState() = AppTopBarState(
    title = "Editar pot",
    subject = "Smart Pot selecionado",
    subjectOnline = true,
    lastUpdateLabel = "Atualize as informacoes do vaso",
)
