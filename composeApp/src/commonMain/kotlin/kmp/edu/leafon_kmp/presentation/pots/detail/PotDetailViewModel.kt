package kmp.edu.leafon_kmp.presentation.pots.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothConnectionStatus
import kmp.edu.leafon_kmp.core.bluetooth.BluetoothTelemetryRepository
import kmp.edu.leafon_kmp.core.bluetooth.NoOpBluetoothTelemetryRepository
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

class PotDetailViewModel(
    private val potId: String,
    private val smartPotRepository: SmartPotRepository = SmartPotRepositoryMemory(),
    private val telemetryRepository: TelemetryRepository = TelemetryRepositoryMemory(),
    private val bluetoothTelemetryRepository: BluetoothTelemetryRepository =
        NoOpBluetoothTelemetryRepository(),
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(PotDetailState(potId = potId))
        private set

    init {
        loadPot()
        observeBluetoothState()
        loadPairedBluetoothDevices()
    }

    fun onAction(action: PotDetailAction) {
        when (action) {
            PotDetailAction.OnEditClick -> Unit
            PotDetailAction.OnViewRoutinesClick -> Unit
            PotDetailAction.OnViewAlertsClick -> Unit
            PotDetailAction.OnRetryClick -> loadPot()
            PotDetailAction.OnReloadBluetoothDevices -> loadPairedBluetoothDevices()
            PotDetailAction.OnConnectBluetoothClick -> connectBluetooth()
            PotDetailAction.OnDisconnectBluetoothClick -> disconnectBluetooth()
            PotDetailAction.OnSyncBluetoothTelemetryClick -> syncBluetoothTelemetry()
            is PotDetailAction.OnBluetoothDeviceSelected -> {
                state = state.copy(
                    selectedBluetoothAddress = action.address,
                    bluetoothErrorMessage = null,
                    bluetoothFeedbackMessage = null,
                )
            }
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
        scope.launch {
            bluetoothTelemetryRepository.disconnect()
        }.invokeOnCompletion {
            scope.cancel()
        }
    }

    private fun loadPot() {
        scope.launch {
            state = state.copy(
                isLoading = true,
                isTelemetryLoading = true,
                errorMessage = null,
                telemetryErrorMessage = null,
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

    private fun observeBluetoothState() {
        scope.launch {
            bluetoothTelemetryRepository.connectionStatus.collect { status ->
                state = state.copy(bluetoothConnectionStatus = status)
            }
        }
        scope.launch {
            bluetoothTelemetryRepository.latestReading.collect { reading ->
                state = state.copy(latestBluetoothReading = reading)
            }
        }
        scope.launch {
            bluetoothTelemetryRepository.errorMessage.collect { message ->
                state = state.copy(bluetoothErrorMessage = message)
            }
        }
    }

    private fun loadPairedBluetoothDevices() {
        scope.launch {
            state = state.copy(
                isLoadingBluetoothDevices = true,
                bluetoothErrorMessage = null,
            )

            try {
                val devices = bluetoothTelemetryRepository.listPairedDevices()
                val selectedAddress = state.selectedBluetoothAddress
                    ?.takeIf { selected -> devices.any { it.address == selected } }
                    ?: devices.firstOrNull()?.address

                state = state.copy(
                    pairedBluetoothDevices = devices,
                    selectedBluetoothAddress = selectedAddress,
                    isLoadingBluetoothDevices = false,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isLoadingBluetoothDevices = false,
                    bluetoothErrorMessage = throwable.message
                        ?: "Nao foi possivel listar dispositivos Bluetooth pareados.",
                )
            }
        }
    }

    private fun connectBluetooth() {
        val address = state.selectedBluetoothAddress
        if (address.isNullOrBlank()) {
            state = state.copy(
                bluetoothErrorMessage = "Selecione um dispositivo Bluetooth pareado.",
                bluetoothFeedbackMessage = null,
            )
            return
        }

        scope.launch {
            state = state.copy(
                bluetoothErrorMessage = null,
                bluetoothFeedbackMessage = null,
            )
            bluetoothTelemetryRepository.connect(address)
        }
    }

    private fun disconnectBluetooth() {
        scope.launch {
            bluetoothTelemetryRepository.disconnect()
            state = state.copy(bluetoothFeedbackMessage = "Bluetooth desconectado.")
        }
    }

    private fun syncBluetoothTelemetry() {
        val reading = state.latestBluetoothReading
        if (reading == null) {
            state = state.copy(
                bluetoothErrorMessage = "Nenhuma leitura Bluetooth valida recebida.",
                bluetoothFeedbackMessage = null,
            )
            return
        }

        if (potId.isBlank()) {
            state = state.copy(
                bluetoothErrorMessage = "Smart Pot invalido para sincronizacao.",
                bluetoothFeedbackMessage = null,
            )
            return
        }

        scope.launch {
            state = state.copy(
                isSyncingBluetoothTelemetry = true,
                bluetoothErrorMessage = null,
                bluetoothFeedbackMessage = null,
            )

            try {
                telemetryRepository.syncBluetoothTelemetry(
                    smartPotId = potId,
                    reading = reading,
                )
                state = state.copy(
                    isSyncingBluetoothTelemetry = false,
                    bluetoothFeedbackMessage = "Telemetria sincronizada com sucesso.",
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                state = state.copy(
                    isSyncingBluetoothTelemetry = false,
                    bluetoothErrorMessage = TelemetryErrorMapper.fromThrowable(
                        throwable = throwable,
                        operation = TelemetryOperation.CREATE,
                    ),
                )
            }
        }
    }
}
