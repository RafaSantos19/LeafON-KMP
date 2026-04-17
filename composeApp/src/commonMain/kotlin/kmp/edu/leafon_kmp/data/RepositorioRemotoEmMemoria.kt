package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi

class RepositorioRemotoEmMemoria : RepositorioRemoto {

    private val pots = mutableListOf(
        PotUi(
            id = "1",
            name = "Aloe Vera",
            plantType = "Suculenta",
            status = PotStatus.ONLINE,
            humidityPercent = 42,
            temperatureCelsius = 24,
            lastUpdateLabel = "2 min atras",
            deviceId = "LEAF-001",
        ),
        PotUi(
            id = "2",
            name = "Monstera Deliciosa",
            plantType = "Tropical",
            status = PotStatus.ONLINE,
            humidityPercent = 68,
            temperatureCelsius = 22,
            lastUpdateLabel = "5 min atras",
            deviceId = "LEAF-002",
        ),
        PotUi(
            id = "3",
            name = "Cacto Sao Jorge",
            plantType = "Cactaceae",
            status = PotStatus.ATTENTION,
            humidityPercent = 18,
            temperatureCelsius = 29,
            lastUpdateLabel = "12 min atras",
            deviceId = "LEAF-003",
        ),
        PotUi(
            id = "4",
            name = "Samambaia",
            plantType = "Pteridofita",
            status = PotStatus.OFFLINE,
            humidityPercent = null,
            temperatureCelsius = null,
            lastUpdateLabel = "3 horas atras",
            deviceId = "",
        ),
        PotUi(
            id = "5",
            name = "Orquidea Phalaenopsis",
            plantType = "Orchidaceae",
            status = PotStatus.ONLINE,
            humidityPercent = 55,
            temperatureCelsius = 23,
            lastUpdateLabel = "1 min atras",
            deviceId = "LEAF-005",
        ),
    )

    private val rotinasPorPot = mutableMapOf(
        "1" to mutableListOf(
            RoutineUi(
                id = "1-morning",
                name = "Irrigacao matinal",
                timeLabel = "08:00",
                daysLabel = "Seg, Qua, Sex",
                durationSec = 20,
                enabled = true,
            ),
            RoutineUi(
                id = "1-evening",
                name = "Revisao da tarde",
                timeLabel = "18:30",
                daysLabel = "Ter, Qui",
                durationSec = 15,
                enabled = true,
            ),
        ),
        "2" to mutableListOf(
            RoutineUi(
                id = "2-morning",
                name = "Irrigacao matinal",
                timeLabel = "08:00",
                daysLabel = "Seg, Qua, Sex",
                durationSec = 20,
                enabled = true,
            ),
            RoutineUi(
                id = "2-evening",
                name = "Revisao da tarde",
                timeLabel = "18:30",
                daysLabel = "Ter, Qui",
                durationSec = 15,
                enabled = true,
            ),
        ),
        "3" to mutableListOf(
            RoutineUi(
                id = "3-low-water",
                name = "Reforco do cacto",
                timeLabel = "07:30",
                daysLabel = "Seg, Sex",
                durationSec = 12,
                enabled = true,
            ),
            RoutineUi(
                id = "3-weekend",
                name = "Fim de semana",
                timeLabel = "09:00",
                daysLabel = "Sab",
                durationSec = 8,
                enabled = false,
            ),
        ),
        "4" to mutableListOf(
            RoutineUi(
                id = "4-recovery",
                name = "Recuperacao",
                timeLabel = "08:15",
                daysLabel = "Ter, Qui, Sab",
                durationSec = 25,
                enabled = false,
            ),
        ),
        "5" to mutableListOf(
            RoutineUi(
                id = "5-morning",
                name = "Irrigacao matinal",
                timeLabel = "08:00",
                daysLabel = "Seg, Qua, Sex",
                durationSec = 20,
                enabled = true,
            ),
            RoutineUi(
                id = "5-evening",
                name = "Revisao da tarde",
                timeLabel = "18:30",
                daysLabel = "Ter, Qui",
                durationSec = 15,
                enabled = true,
            ),
        ),
    )

    override fun getPots(): List<PotUi> {
        return pots.toList()
    }

    override fun getPotById(id: String): PotUi? {
        return pots.firstOrNull { it.id == id }
    }

    override fun postPot(pot: PotUi) {
        pots.add(pot)
    }

    override fun putPot(pot: PotUi) {
        val index = pots.indexOfFirst { it.id == pot.id }

        if (index >= 0) {
            pots[index] = pot
        }
    }

    override fun deletePot(id: String) {
        pots.removeAll { it.id == id }
        rotinasPorPot.remove(id)
    }

    override fun listarRotinas(potId: String): List<RoutineUi> {
        return rotinasPorPot[potId]?.toList().orEmpty()
    }

    override fun criarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinasPorPot.getOrPut(potId) { mutableListOf() }
        rotinas.add(rotina)
    }

    override fun atualizarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinasPorPot.getOrPut(potId) { mutableListOf() }
        val index = rotinas.indexOfFirst { it.id == rotina.id }

        if (index >= 0) {
            rotinas[index] = rotina
        }
    }
}
