package kmp.edu.leafon_kmp.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kmp.edu.leafon_kmp.core.auth.AuthErrorMapper
import kmp.edu.leafon_kmp.core.auth.AuthRepository
import kmp.edu.leafon_kmp.core.auth.AuthResult
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient
import kmp.edu.leafon_kmp.data.remote.dto.CreateUserRequestDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val apiClient: LeafOnApiClient,
    private val onRegisterSuccess: () -> Unit,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    var state by mutableStateOf(RegisterState())
        private set

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnNameChange -> state = state.copy(name = action.value, error = null, infoMessage = null)
            is RegisterAction.OnEmailChange -> state = state.copy(email = action.value, error = null, infoMessage = null)
            is RegisterAction.OnPhoneChange -> state = state.copy(phone = action.value, error = null, infoMessage = null)
            is RegisterAction.OnPasswordChange -> state = state.copy(password = action.value, error = null, infoMessage = null)
            is RegisterAction.OnConfirmPasswordChange -> state = state.copy(confirmPassword = action.value, error = null, infoMessage = null)
            RegisterAction.OnRegisterClick -> register()
        }
    }

    private fun register() {
        val name = state.name.trim()
        val email = state.email.trim()
        val phone = state.phone.trim().ifBlank { null }
        val password = state.password
        val confirmPassword = state.confirmPassword

        when {
            name.isBlank() -> {
                state = state.copy(error = "Informe seu nome.", infoMessage = null)
                return
            }

            email.isBlank() -> {
                state = state.copy(error = "Informe seu email.", infoMessage = null)
                return
            }

            password.isBlank() -> {
                state = state.copy(error = "Informe uma senha.", infoMessage = null)
                return
            }

            password.length < 8 -> {
                state = state.copy(error = "A senha precisa ter pelo menos 8 caracteres.", infoMessage = null)
                return
            }

            password != confirmPassword -> {
                state = state.copy(error = "As senhas nao coincidem.", infoMessage = null)
                return
            }
        }

        state = state.copy(
            name = name,
            email = email,
            phone = phone.orEmpty(),
            isLoading = true,
            error = null,
            infoMessage = null,
        )

        scope.launch {
            when (
                val result = authRepository.signUp(
                    name = name,
                    email = email,
                    password = password,
                    phone = phone,
                )
            ) {
                is AuthResult.Success -> {
                    try {
                        val createdUser = apiClient.createUser(
                            CreateUserRequestDto(
                                email = email,
                                name = name,
                                phone = phone,
                            ),
                        )

                        val isValidResponse = createdUser.email.equals(email, ignoreCase = true)
                        if (!isValidResponse) {
                            authRepository.signOut()
                            state = state.copy(
                                isLoading = false,
                                error = "A resposta do cadastro veio incompleta. Tente novamente.",
                                infoMessage = null,
                            )
                            return@launch
                        }

                        authRepository.signOut()
                        state = state.copy(isLoading = false, error = null, infoMessage = null)
                        onRegisterSuccess()
                    } catch (throwable: Throwable) {
                        authRepository.signOut()
                        state = state.copy(
                            isLoading = false,
                            error = AuthErrorMapper.fromThrowable(throwable),
                            infoMessage = null,
                        )
                    }
                }

                is AuthResult.EmailConfirmationRequired -> {
                    state = state.copy(
                        isLoading = false,
                        error = null,
                        infoMessage = "Cadastro criado. Confirme o email ${result.email} para concluir o acesso.",
                    )
                }

                is AuthResult.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        infoMessage = null,
                    )
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }
}
