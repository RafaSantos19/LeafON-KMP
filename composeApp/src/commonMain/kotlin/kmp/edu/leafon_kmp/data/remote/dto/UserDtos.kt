package kmp.edu.leafon_kmp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

@Serializable
data class CreateUserRequestDto(
    val email: String,
    val name: String? = null,
    val phone: String? = null,
)

@Serializable
data class UpdateUserRequestDto(
    val name: String? = null,
    val phone: String? = null,
)
