package kmp.edu.leafon_kmp.presentation.pots.create

sealed interface CreatePotAction {
    data class OnPlantNameChange(val value: String) : CreatePotAction
    data class OnHumidityMinChange(val value: String) : CreatePotAction
    data class OnDeviceIdChange(val value: String) : CreatePotAction
    data object OnSaveClick : CreatePotAction
}
