package kmp.edu.leafon_kmp.presentation.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// ProfileViewModel.kt
//
// ViewModel para a tela de perfil.
//
// Não estende nenhuma classe específica de plataforma — usa apenas
// kotlinx.coroutines, que é 100% Kotlin Multiplatform.
//
// Para integrar com o ciclo de vida no Android, envolva este ViewModel em
// um androidx.lifecycle.ViewModel e delegue as chamadas. No Desktop/Web,
// instancie-o diretamente e chame onCleared() ao desmontar a tela.
//
// Integração com backend (quando disponível):
//   - Substitua o bloco `delay(1_500)` em [saveChanges] pela chamada real:
//       profileRepository.updateProfile(uiState.value.toRequest())
//   - Substitua o bloco em [logout] pela chamada:
//       authRepository.logout()
// ─────────────────────────────────────────────────────────────────────────────

class ProfileViewModel {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private val _uiState = MutableStateFlow(
        // Estado inicial com dados mockados — substitua pela carga do repositório.
        ProfileUiState(
            firstName = "Lucas",
            lastName  = "da Silva",
            email     = "lucas@example.com",
            phone     = "+55 11 98765-4321",
            gender    = "Male",
        )
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // ── Handlers de campos ────────────────────────────────────────────────────

    fun onFirstNameChange(value: String) =
        _uiState.update { it.copy(firstName = value, hasUnsavedChanges = true) }

    fun onLastNameChange(value: String) =
        _uiState.update { it.copy(lastName = value, hasUnsavedChanges = true) }

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, hasUnsavedChanges = true) }

    fun onPhoneChange(value: String) =
        _uiState.update { it.copy(phone = value, hasUnsavedChanges = true) }

    fun onGenderChange(value: String) =
        _uiState.update { it.copy(gender = value, hasUnsavedChanges = true) }

    fun onCurrentPasswordChange(value: String) =
        _uiState.update { it.copy(currentPassword = value, hasUnsavedChanges = true) }

    fun onNewPasswordChange(value: String) =
        _uiState.update { it.copy(newPassword = value, hasUnsavedChanges = true) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, hasUnsavedChanges = true) }

    // ── Ações ─────────────────────────────────────────────────────────────────

    /**
     * Salva as alterações do perfil.
     * Simula uma chamada de API com delay — substitua pela integração real.
     */
    fun saveChanges() {
        scope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                delay(1_500) // TODO: substituir por profileRepository.updateProfile(...)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasUnsavedChanges = false,
                        saveSuccess = true,
                        // Limpa campos de senha após salvar com sucesso
                        currentPassword = "",
                        newPassword = "",
                        confirmPassword = "",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Failed to save changes.",
                    )
                }
            }
        }
    }

    /**
     * Descarta alterações e restaura os valores originais do estado inicial.
     * Quando integrar o backend, busque novamente o perfil do repositório.
     */
    fun discardChanges() {
        _uiState.update {
            it.copy(
                firstName = "Lucas",
                lastName  = "da Silva",
                email     = "lucas@example.com",
                phone     = "+55 11 98765-4321",
                gender    = "Male",
                currentPassword   = "",
                newPassword       = "",
                confirmPassword   = "",
                hasUnsavedChanges = false,
                errorMessage      = null,
            )
        }
    }

    /**
     * Realiza o logout do usuário.
     * [onLoggedOut] é invocado após a operação para permitir que a camada de
     * navegação redirecione para a tela de Login sem acoplamento ao ViewModel.
     */
    fun logout(onLoggedOut: () -> Unit) {
        scope.launch {
            // TODO: authRepository.logout()
            onLoggedOut()
        }
    }

    /** Limpa o CoroutineScope ao destruir o ViewModel. */
    fun onCleared() {
        job.cancel()
    }
}
