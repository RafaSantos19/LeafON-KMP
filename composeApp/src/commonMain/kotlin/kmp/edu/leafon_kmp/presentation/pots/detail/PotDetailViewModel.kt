package kmp.edu.leafon_kmp.presentation.pots.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus

class PotDetailViewModel(
    private val potId: String,
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

        val pot = getFakePotDetailById(potId)

        state = if (pot == null) {
            state.copy(
                isLoading = false,
                errorMessage = "Pot nao encontrado.",
            )
        } else {
            state.copy(
                potId = pot.id,
                name = pot.name,
                plantName = pot.plantName,
                isOnline = pot.status == PotStatus.ONLINE,
                status = pot.status,
                humidityPercent = pot.humidityPercent,
                temperatureCelsius = pot.temperatureCelsius,
                lightLevel = pot.lightLevel,
                lastUpdateLabel = pot.lastUpdateLabel,
                isLoading = false,
                errorMessage = null,
            )
        }
    }
}

private data class FakePotDetail(
    val id: String,
    val name: String,
    val plantName: String,
    val status: PotStatus,
    val humidityPercent: Int?,
    val temperatureCelsius: Int?,
    val lightLevel: Int?,
    val lastUpdateLabel: String,
)

private fun getFakePotDetailById(id: String): FakePotDetail? {
    return fakePotDetails().firstOrNull { it.id == id }
}

private fun fakePotDetails() = listOf(
    FakePotDetail(
        id = "1",
        name = "Aloe Vera",
        plantName = "Aloe Vera",
        status = PotStatus.ONLINE,
        humidityPercent = 42,
        temperatureCelsius = 24,
        lightLevel = 76,
        lastUpdateLabel = "2 min atras",
    ),
    FakePotDetail(
        id = "2",
        name = "Monstera Deliciosa",
        plantName = "Monstera",
        status = PotStatus.ONLINE,
        humidityPercent = 68,
        temperatureCelsius = 22,
        lightLevel = 61,
        lastUpdateLabel = "5 min atras",
    ),
    FakePotDetail(
        id = "3",
        name = "Cacto Sao Jorge",
        plantName = "Cacto Sao Jorge",
        status = PotStatus.ATTENTION,
        humidityPercent = 18,
        temperatureCelsius = 29,
        lightLevel = 88,
        lastUpdateLabel = "12 min atras",
    ),
    FakePotDetail(
        id = "4",
        name = "Samambaia",
        plantName = "Samambaia",
        status = PotStatus.OFFLINE,
        humidityPercent = null,
        temperatureCelsius = null,
        lightLevel = null,
        lastUpdateLabel = "3 horas atras",
    ),
    FakePotDetail(
        id = "5",
        name = "Orquidea Phalaenopsis",
        plantName = "Orquidea",
        status = PotStatus.ONLINE,
        humidityPercent = 55,
        temperatureCelsius = 23,
        lightLevel = 54,
        lastUpdateLabel = "1 min atras",
    ),
)
