package kmp.edu.leafon_kmp.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterViewModel {

    var state by mutableStateOf(RegisterState())
        private set

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNameChange -> state = state.copy(name = action.value)
            is RegisterAction.OnEmailChange -> state = state.copy(email = action.value)
            is RegisterAction.OnPhoneChange -> state = state.copy(phone = action.value)
            is RegisterAction.OnPlantNameChange -> state = state.copy(plantName = action.value)
            is RegisterAction.OnPasswordChange -> state = state.copy(password = action.value)
            is RegisterAction.OnConfirmPasswordChange -> state = state.copy(confirmPassword = action.value)
            RegisterAction.OnRegisterClick -> register()
        }
    }

    private fun register() {
        println("Registrando: ${state.email}")
    }
}
