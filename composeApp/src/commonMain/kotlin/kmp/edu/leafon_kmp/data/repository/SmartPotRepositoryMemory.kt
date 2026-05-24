package kmp.edu.leafon_kmp.data.repository

import kmp.edu.leafon_kmp.core.model.SmartPot
import kmp.edu.leafon_kmp.core.network.ApiException

class SmartPotRepositoryMemory : SmartPotRepository {
    private val smartPots = mutableListOf<SmartPot>()
    private var nextId = 1

    override suspend fun listSmartPots(): List<SmartPot> {
        return smartPots.toList()
    }

    override suspend fun getSmartPotById(id: String): SmartPot {
        return smartPots.firstOrNull { it.id == id }
            ?: throw ApiException(404, "SmartPot nao encontrado.")
    }

    override suspend fun createSmartPot(
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot {
        validateHumidity(humidityMin)
        validateDuplicatedDeviceId(deviceId)

        val created = SmartPot(
            id = nextId.toString(),
            plantName = plantName.trim(),
            humidityMin = humidityMin,
            deviceId = deviceId?.trim()?.takeIf { it.isNotBlank() },
        )

        nextId += 1
        smartPots.add(created)
        return created
    }

    override suspend fun updateSmartPot(
        id: String,
        plantName: String,
        humidityMin: Int,
        deviceId: String?,
    ): SmartPot {
        validateHumidity(humidityMin)

        val current = getSmartPotById(id)
        validateDuplicatedDeviceId(deviceId, ignoredId = id)

        val updated = current.copy(
            plantName = plantName.trim(),
            humidityMin = humidityMin,
            deviceId = deviceId?.trim()?.takeIf { it.isNotBlank() },
        )

        val index = smartPots.indexOfFirst { it.id == id }
        smartPots[index] = updated
        return updated
    }

    override suspend fun deleteSmartPot(id: String) {
        val removed = smartPots.removeAll { it.id == id }

        if (!removed) {
            throw ApiException(404, "SmartPot nao encontrado.")
        }
    }

    private fun validateHumidity(humidityMin: Int) {
        if (humidityMin !in 0..100) {
            throw ApiException(400, "humidityMin precisa estar entre 0 e 100.")
        }
    }

    private fun validateDuplicatedDeviceId(
        deviceId: String?,
        ignoredId: String? = null,
    ) {
        val normalizedDeviceId = deviceId?.trim()?.takeIf { it.isNotBlank() } ?: return
        val duplicated = smartPots.any { pot ->
            pot.id != ignoredId && pot.deviceId == normalizedDeviceId
        }

        if (duplicated) {
            throw ApiException(409, "Device ID duplicado.")
        }
    }
}
