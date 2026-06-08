package kmp.edu.leafon_kmp.core.bluetooth

actual fun createBluetoothTelemetryRepository(): BluetoothTelemetryRepository =
    NoOpBluetoothTelemetryRepository()
