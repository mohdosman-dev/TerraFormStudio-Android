package com.otaku.terraformstudio.features.auth.presentation.signup

import com.otaku.terraformstudio.core.domain.DataError

data class SignUpState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isNewsletterChecked: Boolean = false,
    val isLoading: Boolean = false,
    val error: DataError.Network? = null,
    val isPasswordVisible: Boolean = false
)

sealed interface SignUpAction {
    data class OnFullNameChange(val value: String) : SignUpAction
    data class OnEmailChange(val value: String) : SignUpAction
    data class OnPasswordChange(val value: String) : SignUpAction
    data object OnTogglePasswordVisibility : SignUpAction
    data class OnNewsletterCheckChange(val value: Boolean) : SignUpAction
    data object OnSignUpClick : SignUpAction
    data object OnSignInClick : SignUpAction
}

sealed interface SignUpEvent {
    data class Error(val error: DataError.Network) : SignUpEvent
    data object SignUpSuccess : SignUpEvent
    data object NavigateToSignIn : SignUpEvent
}
