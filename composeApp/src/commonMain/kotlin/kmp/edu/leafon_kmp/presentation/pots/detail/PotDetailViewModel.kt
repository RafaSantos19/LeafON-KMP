package kmp.edu.leafon_kmp.presentation.pots.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.data.RepositorioRemoto
import kmp.edu.leafon_kmp.data.RepositorioRemotoEmMemoria
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

class PotDetailViewModel(
    private val potId: String,
    private val repositorio: RepositorioRemoto = RepositorioRemotoEmMemoria(),
) {

    var state by mutableStateOf(PotDetailState(potId = potId))
        private set

    init {
        loadPot()
    }

    fun onAction(action: PotDetailAction) {
        when (action) {
            PotDetailAction.OnEditClick -> Unit
            PotDetailAction.OnViewRoutinesClick -> Unit
            PotDetailAction.OnViewAlertsClick -> Unit
            PotDetailAction.OnRetryClick -> loadPot()
        }
    }

    private fun loadPot() {
        state = state.copy(isLoading = true, errorMessage = null)

        val pot = repositorio.getPotById(potId)

        state = if (pot == null) {
            state.copy(
                isLoading = false,
                errorMessage = "Pot nao encontrado.",
            )
        } else {
            state.copy(
                potId = pot.id,
                name = pot.name,
                plantName = pot.plantType,
                isOnline = pot.status == PotStatus.ONLINE,
                status = pot.status,
                humidityPercent = pot.humidityPercent,
                temperatureCelsius = pot.temperatureCelsius,
                lightLevel = null,
                lastUpdateLabel = pot.lastUpdateLabel,
                isLoading = false,
                errorMessage = null,
            )
        }
    }
}
