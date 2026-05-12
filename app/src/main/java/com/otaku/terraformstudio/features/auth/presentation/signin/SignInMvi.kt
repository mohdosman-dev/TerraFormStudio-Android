package com.otaku.terraformstudio.features.auth.presentation.signin

import com.otaku.terraformstudio.core.domain.DataError

data class SignInState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val error: DataError.Network? = null,
    val isPasswordVisible: Boolean = false
)

sealed interface SignInAction {
    data class OnEmailChange(val value: String) : SignInAction
    data class OnPasswordChange(val value: String) : SignInAction
    data object OnTogglePasswordVisibility : SignInAction
    data object OnSignInClick : SignInAction
    data object OnSignUpClick : SignInAction
}

sealed interface SignInEvent {
    data class Error(val error: DataError.Network) : SignInEvent
    data object SignInSuccess : SignInEvent
    data object NavigateToSignUp : SignInEvent
}
