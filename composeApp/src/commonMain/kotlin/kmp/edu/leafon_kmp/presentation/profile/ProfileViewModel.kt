package kmp.edu.leafon_kmp.presentation.profile

import kmp.edu.leafon_kmp.core.auth.AuthErrorMapper
import kmp.edu.leafon_kmp.core.auth.AuthRepository
import kmp.edu.leafon_kmp.data.remote.LeafOnApiClient
import kmp.edu.leafon_kmp.data.remote.dto.UpdateUserRequestDto
import kmp.edu.leafon_kmp.data.remote.dto.UserResponseDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val apiClient: LeafOnApiClient,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var persistedProfile = PersistedProfile()
    private var initialLoadStarted = false

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        loadProfile()
    }

    fun onNameChange(value: String) {
        _state.update { current ->
            current.copy(name = value, hasUnsavedChanges = true, saveSuccess = false)
        }
    }

    fun onPhoneChange(value: String) {
        _state.update { current ->
            current.copy(phone = value, hasUnsavedChanges = true, saveSuccess = false)
        }
    }

    fun saveChanges() {
        val currentState = state.value
        val request = UpdateUserRequestDto(
            name = currentState.name.trim().ifBlank { null },
            phone = currentState.phone.trim().ifBlank { null },
        )

        scope.launch {
            _state.update { current ->
                current.copy(isSaving = true, errorMessage = null, saveSuccess = false)
            }

            try {
                val updatedUser = apiClient.updateMe(request)
                persistedProfile = updatedUser.toPersistedProfile(
                    fallback = PersistedProfile(
                        name = currentState.name.trim(),
                        email = currentState.email.trim(),
                        phone = currentState.phone.trim(),
                    ),
                )

                _state.value = persistedProfile.toUiState(
                    isSaving = false,
                    hasUnsavedChanges = false,
                    saveSuccess = true,
                    errorMessage = null,
                )
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                _state.update { current ->
                    current.copy(
                        isSaving = false,
                        errorMessage = AuthErrorMapper.fromThrowable(throwable),
                        saveSuccess = false,
                    )
                }
            }
        }
    }

    fun discardChanges() {
        _state.value = persistedProfile.toUiState(
            isLoading = false,
            isSaving = false,
            hasUnsavedChanges = false,
            saveSuccess = false,
            errorMessage = null,
        )
    }

    fun logout(onLoggedOut: () -> Unit) {
        scope.launch {
            try {
                authRepository.signOut()
                onLoggedOut()
            } catch (throwable: CancellationException) {
                throw throwable
            } catch (throwable: Throwable) {
                _state.update { current ->
                    current.copy(errorMessage = AuthErrorMapper.fromThrowable(throwable))
                }
            }
        }
    }

    fun onCleared() {
        scope.cancel()
    }

    private fun loadProfile() {
        if (initialLoadStarted) {
            println("ProfileViewModel.loadProfile -> skipped duplicate start")
            return
        }
        initialLoadStarted = true

        scope.launch {
            println("ProfileViewModel.loadProfile -> start")
            _state.update { current ->
                current.copy(isLoading = true, errorMessage = null, saveSuccess = false)
            }

            try {
                val user = apiClient.getMe()
                println("ProfileViewModel.loadProfile -> user=$user")
                persistedProfile = user.toPersistedProfile()
                println("ProfileViewModel.loadProfile -> persistedProfile=$persistedProfile")

                _state.value = persistedProfile.toUiState(
                    isLoading = false,
                    hasUnsavedChanges = false,
                    saveSuccess = false,
                    errorMessage = null,
                )
                println("ProfileViewModel.loadProfile -> finalState=${_state.value}")
            } catch (throwable: CancellationException) {
                println("ProfileViewModel.loadProfile -> cancelled: ${throwable.message}")
                throw throwable
            } catch (throwable: Throwable) {
                println("ProfileViewModel.loadProfile -> error=$throwable")
                println("ProfileViewModel.loadProfile -> mappedError=${AuthErrorMapper.fromThrowable(throwable)}")
                _state.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = AuthErrorMapper.fromThrowable(throwable),
                        saveSuccess = false,
                    )
                }
            }
        }
    }

    private fun UserResponseDto.toPersistedProfile(
        fallback: PersistedProfile = PersistedProfile(),
    ): PersistedProfile {
        return PersistedProfile(
            name = name.orEmpty().trim().ifBlank { fallback.name },
            email = email.orEmpty().trim().ifBlank { fallback.email },
            phone = phone.orEmpty().trim().ifBlank { fallback.phone },
        )
    }

    private data class PersistedProfile(
        val name: String = "",
        val email: String = "",
        val phone: String = "",
    ) {
        fun toUiState(
            isLoading: Boolean = false,
            isSaving: Boolean = false,
            hasUnsavedChanges: Boolean = false,
            saveSuccess: Boolean = false,
            errorMessage: String? = null,
        ): ProfileUiState {
            return ProfileUiState(
                name = name,
                email = email,
                phone = phone,
                isLoading = isLoading,
                isSaving = isSaving,
                hasUnsavedChanges = hasUnsavedChanges,
                saveSuccess = saveSuccess,
                errorMessage = errorMessage,
            )
        }
    }
}
