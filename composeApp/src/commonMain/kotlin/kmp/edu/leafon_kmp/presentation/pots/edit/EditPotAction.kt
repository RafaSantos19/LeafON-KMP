package kmp.edu.leafon_kmp.presentation.pots.edit

sealed interface EditPotAction {
    data class OnNameChange(val value: String) : EditPotAction
    data class OnPlantNameChange(val value: String) : EditPotAction
    data class OnDeviceIdChange(val value: String) : EditPotAction
    data object OnSaveClick : EditPotAction
}
