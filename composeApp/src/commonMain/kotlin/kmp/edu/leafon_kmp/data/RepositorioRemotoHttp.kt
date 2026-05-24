package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RepositorioRemotoHttp(
    private val smartPotRepository: SmartPotRepository,
    private val fallback: RepositorioRemoto = RepositorioRemotoEmMemoria(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : RepositorioRemoto {

    private val potCache = mutableListOf<PotUi>().apply {
        addAll(fallback.getPots())
    }

    private val rotinaCache = mutableMapOf<String, MutableList<RoutineUi>>()

    override fun getPots(): List<PotUi> {
        refreshPotsAsync()
        return potCache.toList()
    }

    override fun getPotById(id: String): PotUi? {
        refreshPotAsync(id)
        return potCache.firstOrNull { it.id == id }
    }

    override fun postPot(pot: PotUi) {
        potCache.removeAll { it.id == pot.id }
        potCache.add(pot)

        scope.launch {
            runCatching {
                smartPotRepository.createSmartPot(
                    plantName = pot.plantType.ifBlank { pot.name },
                    humidityMin = pot.humidityPercent ?: DEFAULT_HUMIDITY_MIN,
                    deviceId = pot.deviceId.takeIf { it.isNotBlank() },
                )
            }.onSuccess { created ->
                replacePot(created)
            }
        }
    }

    override fun putPot(pot: PotUi) {
        val index = potCache.indexOfFirst { it.id == pot.id }

        if (index >= 0) {
            potCache[index] = pot
        } else {
            potCache.add(pot)
        }

        scope.launch {
            runCatching {
                smartPotRepository.updateSmartPot(
                    id = pot.id,
                    plantName = pot.plantType.ifBlank { pot.name },
                    humidityMin = pot.humidityPercent ?: DEFAULT_HUMIDITY_MIN,
                    deviceId = pot.deviceId.takeIf { it.isNotBlank() },
                )
            }.onSuccess { updated ->
                replacePot(updated)
            }
        }
    }

    override fun deletePot(id: String) {
        potCache.removeAll { it.id == id }
        rotinaCache.remove(id)

        scope.launch {
            runCatching { smartPotRepository.deleteSmartPot(id) }
        }
    }

    override fun listarRotinas(potId: String): List<RoutineUi> {
        return rotinaCache[potId]?.toList() ?: fallback.listarRotinas(potId)
    }

    override fun criarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinaCache.getOrPut(potId) {
            fallback.listarRotinas(potId).toMutableList()
        }
        rotinas.removeAll { it.id == rotina.id }
        rotinas.add(rotina)
    }

    override fun atualizarRotina(potId: String, rotina: RoutineUi) {
        val rotinas = rotinaCache.getOrPut(potId) {
            fallback.listarRotinas(potId).toMutableList()
        }
        val index = rotinas.indexOfFirst { it.id == rotina.id }

        if (index >= 0) {
            rotinas[index] = rotina
        } else {
            rotinas.add(rotina)
        }
    }

    suspend fun syncPotsFromBackend(): List<PotUi> {
        val pots = smartPotRepository.listSmartPots().map { it.toLegacyPotUi() }

        potCache.clear()
        potCache.addAll(pots)
        return potCache.toList()
    }

    private fun refreshPotsAsync() {
        scope.launch {
            runCatching { syncPotsFromBackend() }
        }
    }

    private fun refreshPotAsync(id: String) {
        scope.launch {
            runCatching {
                replacePot(smartPotRepository.getSmartPotById(id))
            }
        }
    }

    private fun replacePot(remotePot: SmartPot) {
        val mappedPot = remotePot.toLegacyPotUi()
        val index = potCache.indexOfFirst { it.id == mappedPot.id }

        if (index >= 0) {
            potCache[index] = mappedPot
        } else {
            potCache.add(mappedPot)
        }
    }
}

private fun SmartPot.toLegacyPotUi(): PotUi {
    return PotUi(
        id = id,
        name = plantName,
        plantType = plantName,
        status = PotStatus.OFFLINE,
        humidityPercent = humidityMin,
        temperatureCelsius = null,
        lastUpdateLabel = updatedAt ?: createdAt ?: "Sem telemetria integrada",
        deviceId = deviceId.orEmpty(),
    )
}

private const val DEFAULT_HUMIDITY_MIN = 30
