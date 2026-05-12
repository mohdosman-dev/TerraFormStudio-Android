package com.otaku.terraformstudio.features.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignupRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String? = null,
    val user: UserDto,
    val message: String? = null
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val roles: List<String> = emptyList()
)

@Serializable
data class MeResponse(
    val user: UserDto
)
