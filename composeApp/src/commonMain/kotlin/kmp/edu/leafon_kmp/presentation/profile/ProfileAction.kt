package kmp.edu.leafon_kmp.presentation.profile

sealed interface ProfileAction {
    data class OnNameChanged(val value: String) : ProfileAction
    data class OnPhoneChanged(val value: String) : ProfileAction
    data object OnSaveClicked : ProfileAction
    data object OnDiscardClicked : ProfileAction
    data object OnLogoutClicked : ProfileAction
}
