package kmp.edu.leafon_kmp.presentation.pots.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.time.IsoTimestampProvider
import kmp.edu.leafon_kmp.data.repository.SmartPotRepository
import kmp.edu.leafon_kmp.data.repository.SmartPotRepositoryMemory
import kmp.edu.leafon_kmp.data.repository.TelemetryRepository
import kmp.edu.leafon_kmp.data.repository.TelemetryRepositoryMemory
import kmp.edu.leafon_kmp.presentation.pots.SmartPotErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.SmartPotOperation
import kmp.edu.leafon_kmp.presentation.pots.TelemetryErrorMapper
import kmp.edu.leafon_kmp.presentation.pots.TelemetryOperation
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.random.Random

class PotDetailViewModel(
    private val potId: String,
    private val smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
    private val telemetryRepository: TelemetryRepository = TelemetryRepositoryMemory(),
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(PotDetailState(potId = potId))
        private set

    init {
        loadPot()
    }

    fun onAction(action: PotDetailAction) {
        when (action) {
            PotDetailAction.OnEditClick -> Unit
            PotDetailAction.OnGenerateTelemetryClick -> generateFakeTelemetry()
            PotDetailAction.OnViewRoutinesClick -> Unit
            PotDetailAction.OnViewAlertsClick -> Unit
            PotDetailAction.OnRetryClick -> loadPot()
        }
    }

    fun deletePot(onDeleted: () -> Unit) {
        scope.launch {
            state = state.copy(isDeleting = true, errorMessage = null)

            try {
                smartPotRepository.deleteSmartPot(potId)
                state = state.copy(isDeleting = false, errorMessage = null)
                onDeleted()
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isDeleting = false,
                    errorMessage = SmartPotErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = SmartPotOperation.DELETE,
                    ),
                )
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadPot() {
        scope.launch {
            state = state.copy(
                isLoading = true,
                isTelemetryLoading = true,
                errorMessage = null,
                telemetryErrorMessage = null,
                feedbackMessage = null,
            )

            try {
                val pot = smartPotRepository.getSmartPotById(potId)
                val latestTelemetry = try {
                    telemetryRepository.getLatestTelemetry(pot.id)
                } catch (throwable: CancellationException) {
                    throw throwable
                } catch (throwable: Throwable) {
                    state = state.copy(
                        telemetryErrorMessage = TelemetryErrorMapper.fromThrowable(
                            throwable = throwable,
                            operation = TelemetryOperation.LOAD_LATEST,
                        ),
                    )
                    null
                }

                state = state.copy(
                    potId = pot.id,
                    plantName = pot.plantName,
                    humidityMin = pot.humidityMin,
                    deviceId = pot.deviceId,
                    createdAt = pot.createdAt,
                    updatedAt = pot.updatedAt,
                    isLoading = false,
                    isTelemetryLoading = false,
                    latestTelemetry = latestTelemetry,
                    errorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoading = false,
                    isTelemetryLoading = false,
                    errorMessage = SmartPotErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = SmartPotOperation.DETAIL,
                    ),
                )
            }
        }
    }

    private fun generateFakeTelemetry() {
        val smartPotId = state.potId.trim()
        val humidityMin = state.humidityMin

        if (smartPotId.isBlank()) {
            state = state.copy(
                telemetryErrorMessage = "Vaso nao encontrado.",
                feedbackMessage = null,
            )
            return
        }

        val soilHumidity = generateSoilHumidity(humidityMin)
        val temperature = Random.nextDouble(from = 18.0, until = 35.0)
        val luminosity = Random.nextDouble(from = 100.0, until = 1000.0)
        val readAt = IsoTimestampProvider.nowUtc()

        if (soilHumidity !in 0..100 || readAt.isBlank()) {
            state = state.copy(
                telemetryErrorMessage = "Leitura invalida. Verifique os valores enviados.",
                feedbackMessage = null,
            )
            return
        }

        scope.launch {
            state = state.copy(
                isSendingTelemetry = true,
                telemetryErrorMessage = null,
                feedbackMessage = null,
            )

            try {
                val created = telemetryRepository.createTelemetry(
                    smartPotId = smartPotId,
                    soilHumidity = soilHumidity,
                    temperature = temperature,
                    luminosity = luminosity,
                    readAt = readAt,
                )

                state = state.copy(
                    isSendingTelemetry = false,
                    latestTelemetry = created,
                    telemetryErrorMessage = null,
                    feedbackMessage = "Leitura de teste enviada com sucesso.",
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isSendingTelemetry = false,
                    telemetryErrorMessage = TelemetryErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = TelemetryOperation.CREATE,
                    ),
                    feedbackMessage = null,
                )
            }
        }
    }

    private fun generateSoilHumidity(humidityMin: Int?): Int {
        val threshold = humidityMin ?: 40
        val shouldGoBelowThreshold = Random.nextBoolean()
        val normalizedThreshold = threshold.coerceIn(0, 100)

        return if (shouldGoBelowThreshold) {
            Random.nextInt(from = 0, until = (normalizedThreshold + 1).coerceAtMost(101))
        } else {
            Random.nextInt(from = normalizedThreshold, until = 101)
        }.coerceIn(0, 100)
    }
}
