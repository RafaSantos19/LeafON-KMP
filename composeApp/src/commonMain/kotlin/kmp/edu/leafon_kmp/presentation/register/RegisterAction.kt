package kmp.edu.leafon_kmp.presentation.register

sealed class RegisterAction {
    data class OnNameChange(val value: String) : RegisterAction()
    data class OnEmailChange(val value: String) : RegisterAction()
    data class OnPhoneChange(val value: String) : RegisterAction()
    data class OnPlantNameChange(val value: String) : RegisterAction()
    data class OnPasswordChange(val value: String) : RegisterAction()
    data class OnConfirmPasswordChange(val value: String) : RegisterAction()
    object OnRegisterClick : RegisterAction()
}
