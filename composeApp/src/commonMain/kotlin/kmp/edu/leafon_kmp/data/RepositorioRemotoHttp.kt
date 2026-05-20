package kmp.edu.leafon_kmp.data

import kmp.edu.leafon_kmp.core.model.TelemetryReading
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient
import kmp.edu.leafon_kmp.data.remote.dto.CreateSmartPotRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.SmartPotResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.TelemetryResponseDto
import kmp.edu.leafon_kmp.data.remote.dto.UpdateSmartPotRequestDto
import kmp.edu.leafon_kmp.presentation.pots.model.PotStatus
import kmp.edu.leafon_kmp.presentation.pots.model.PotUi
import kmp.edu.leafon_kmp.presentation.pots.routines.model.RoutineUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class RepositorioRemotoHttp(
    private val apiClient: LeafOnApiClient,
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
                apiClient.createSmartPot(pot.toCreateRequest())
            }.onSuccess { created ->
                replacePot(
                    remotePot = created,
                    latestTelemetry = apiClient.safeLatestTelemetry(created.id),
                )
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
                apiClient.updateSmartPot(pot.id, pot.toUpdateRequest())
            }.onSuccess { updated ->
                replacePot(
                    remotePot = updated,
                    latestTelemetry = apiClient.safeLatestTelemetry(updated.id),
                )
            }
        }
    }

    override fun deletePot(id: String) {
        potCache.removeAll { it.id == id }
        rotinaCache.remove(id)

        scope.launch {
            runCatching { apiClient.deleteSmartPot(id) }
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
        val pots = apiClient.getSmartPots().map { remotePot ->
            val latestTelemetry = apiClient.safeLatestTelemetry(remotePot.id)?.toDomain()
            remotePot.toDomain(latestTelemetry)
        }

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
                val remotePot = apiClient.getSmartPotById(id)
                replacePot(
                    remotePot = remotePot,
                    latestTelemetry = apiClient.safeLatestTelemetry(remotePot.id),
                )
            }
        }
    }

    private suspend fun replacePot(
        remotePot: SmartPotResponseDto,
        latestTelemetry: TelemetryResponseDto?,
    ) {
        val mappedPot = remotePot.toDomain(latestTelemetry?.toDomain())
        val index = potCache.indexOfFirst { it.id == mappedPot.id }

        if (index >= 0) {
            potCache[index] = mappedPot
        } else {
            potCache.add(mappedPot)
        }
    }
}

private suspend fun LeafOnApiClient.safeLatestTelemetry(
    smartPotId: String?,
): TelemetryResponseDto? {
    val resolvedId = smartPotId?.takeIf { it.isNotBlank() } ?: return null
    return runCatching { getLatestTelemetry(resolvedId) }.getOrNull()
}

private fun SmartPotResponseDto.toDomain(
    latestTelemetry: TelemetryReading?,
): PotUi {
    val resolvedId = id.orEmpty().ifBlank { deviceId.orEmpty() }
    val plantLabel = plantName.ifBlank { "Smart Pot" }

    return PotUi(
        id = resolvedId,
        name = plantLabel,
        plantType = plantLabel,
        status = latestTelemetry.toPotStatus(humidityMin),
        humidityPercent = latestTelemetry?.soilHumidity,
        temperatureCelsius = latestTelemetry?.temperature?.roundToInt(),
        lastUpdateLabel = latestTelemetry?.readAt?.ifBlank { "Sem telemetria recente" }
            ?: "Sem telemetria recente",
        deviceId = deviceId.orEmpty(),
    )
}

private fun PotUi.toCreateRequest(): CreateSmartPotRequestDto {
    return CreateSmartPotRequestDto(
        plantName = plantType.ifBlank { name },
        humidityMin = humidityPercent ?: DEFAULT_HUMIDITY_MIN,
        deviceId = deviceId.takeIf { it.isNotBlank() },
    )
}

private fun PotUi.toUpdateRequest(): UpdateSmartPotRequestDto {
    return UpdateSmartPotRequestDto(
        plantName = plantType.ifBlank { name },
        humidityMin = humidityPercent ?: DEFAULT_HUMIDITY_MIN,
        deviceId = deviceId.takeIf { it.isNotBlank() },
    )
}

private fun TelemetryResponseDto.toDomain(): TelemetryReading {
    return TelemetryReading(
        id = id.orEmpty(),
        smartPotId = smartPotId.orEmpty(),
        soilHumidity = soilHumidity,
        temperature = temperature,
        luminosity = luminosity,
        readAt = readAt,
        createdAt = createdAt,
    )
}

private fun TelemetryReading?.toPotStatus(humidityMin: Int): PotStatus {
    val reading = this ?: return PotStatus.OFFLINE

    return if (reading.soilHumidity < humidityMin) {
        PotStatus.ATTENTION
    } else {
        PotStatus.ONLINE
    }
}

private const val DEFAULT_HUMIDITY_MIN = 30
