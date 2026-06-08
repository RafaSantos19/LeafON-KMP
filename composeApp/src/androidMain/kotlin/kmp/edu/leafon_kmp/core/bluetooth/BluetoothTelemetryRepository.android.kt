package kmp.edu.leafon_kmp.core.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import kmp.edu.leafon_kmp.core.time.IsoTimestampProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

actual fun createBluetoothTelemetryRepository(): BluetoothTelemetryRepository =
    AndroidBluetoothTelemetryRepository()

@SuppressLint("MissingPermission")
private class AndroidBluetoothTelemetryRepository : BluetoothTelemetryRepository {
    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val mutableConnectionStatus = MutableStateFlow(
        if (adapter == null) {
            BluetoothConnectionStatus.UNAVAILABLE
        } else {
            BluetoothConnectionStatus.DISCONNECTED
        }
    )
    private val mutableLatestReading = MutableStateFlow<BluetoothTelemetryReading?>(null)
    private val mutableErrorMessage = MutableStateFlow<String?>(null)

    override val connectionStatus: StateFlow<BluetoothConnectionStatus> =
        mutableConnectionStatus
    override val latestReading: StateFlow<BluetoothTelemetryReading?> = mutableLatestReading
    override val errorMessage: StateFlow<String?> = mutableErrorMessage

    private var socket: BluetoothSocket? = null
    private var reader: BufferedReader? = null
    private var readingJob: Job? = null

    override suspend fun listPairedDevices(): List<PairedBluetoothDevice> =
        withContext(Dispatchers.IO) {
            val bluetoothAdapter = adapter
                ?: return@withContext emptyList()

            try {
                bluetoothAdapter.bondedDevices
                    .map { device ->
                        PairedBluetoothDevice(
                            name = device.name?.takeIf { it.isNotBlank() }
                                ?: "Dispositivo sem nome",
                            address = device.address,
                        )
                    }
                    .sortedBy { it.name.lowercase() }
            } catch (exception: SecurityException) {
                mutableConnectionStatus.value = BluetoothConnectionStatus.ERROR
                mutableErrorMessage.value = PERMISSION_ERROR
                emptyList()
            }
        }

    override suspend fun connect(address: String) {
        disconnect()

        val bluetoothAdapter = adapter
        if (bluetoothAdapter == null) {
            mutableConnectionStatus.value = BluetoothConnectionStatus.UNAVAILABLE
            mutableErrorMessage.value = "Bluetooth nao esta disponivel neste dispositivo."
            return
        }

        mutableConnectionStatus.value = BluetoothConnectionStatus.CONNECTING
        mutableErrorMessage.value = null

        try {
            val device = bluetoothAdapter.getRemoteDevice(address)
            val connectedSocket = withContext(Dispatchers.IO) {
                device.createRfcommSocketToServiceRecord(SPP_UUID).also { it.connect() }
            }
            val connectedReader = BufferedReader(
                InputStreamReader(connectedSocket.inputStream, Charsets.UTF_8)
            )

            socket = connectedSocket
            reader = connectedReader
            mutableConnectionStatus.value = BluetoothConnectionStatus.CONNECTED
            startReading(connectedReader)
        } catch (exception: SecurityException) {
            closeResources()
            mutableConnectionStatus.value = BluetoothConnectionStatus.ERROR
            mutableErrorMessage.value = PERMISSION_ERROR
        } catch (exception: Throwable) {
            if (exception is CancellationException) throw exception
            closeResources()
            mutableConnectionStatus.value = BluetoothConnectionStatus.ERROR
            mutableErrorMessage.value =
                exception.message ?: "Nao foi possivel conectar ao dispositivo Bluetooth."
        }
    }

    override suspend fun disconnect() {
        val job = readingJob
        readingJob = null
        closeResources()
        job?.cancelAndJoin()

        if (adapter != null) {
            mutableConnectionStatus.value = BluetoothConnectionStatus.DISCONNECTED
        }
        mutableErrorMessage.value = null
    }

    private fun startReading(connectedReader: BufferedReader) {
        readingJob?.cancel()
        readingJob = scope.launch {
            try {
                while (isActive) {
                    val line = connectedReader.readLine() ?: break
                    parseBluetoothTelemetryReading(
                        line = line,
                        receivedAt = IsoTimestampProvider.nowUtc(),
                    )?.let { reading ->
                        mutableLatestReading.value = reading
                    }
                }

                if (isActive) {
                    closeResources()
                    mutableConnectionStatus.value = BluetoothConnectionStatus.DISCONNECTED
                }
            } catch (exception: Throwable) {
                if (exception is CancellationException) throw exception
                closeResources()
                mutableConnectionStatus.value = BluetoothConnectionStatus.ERROR
                mutableErrorMessage.value =
                    exception.message ?: "A conexao Bluetooth foi interrompida."
            }
        }
    }

    private fun closeResources() {
        runCatching { reader?.close() }
        runCatching { socket?.close() }
        reader = null
        socket = null
    }

    private companion object {
        val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        const val PERMISSION_ERROR =
            "Permissao para acessar dispositivos Bluetooth nao concedida."
    }
}
