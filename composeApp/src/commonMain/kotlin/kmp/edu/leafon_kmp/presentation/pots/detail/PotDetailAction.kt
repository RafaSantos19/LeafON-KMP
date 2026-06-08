package kmp.edu.leafon_kmp.presentation.pots.detail

sealed interface PotDetailAction {
    data object OnEditClick : PotDetailAction
    data object OnViewRoutinesClick : PotDetailAction
    data object OnViewAlertsClick : PotDetailAction
    data object OnRetryClick : PotDetailAction
    data object OnReloadBluetoothDevices : PotDetailAction
    data object OnConnectBluetoothClick : PotDetailAction
    data object OnDisconnectBluetoothClick : PotDetailAction
    data object OnSyncBluetoothTelemetryClick : PotDetailAction
    data class OnBluetoothDeviceSelected(val address: String) : PotDetailAction
}
