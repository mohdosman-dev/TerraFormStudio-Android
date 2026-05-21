package com.otaku.terraformstudio.features.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.data.local.GuestTokenManager
import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.onFailure
import com.otaku.terraformstudio.core.domain.onSuccess
import com.otaku.terraformstudio.features.auth.domain.AuthRepository
import com.otaku.terraformstudio.features.cart.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val guestTokenManager: GuestTokenManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignUpEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnEmailChange -> _state.update {
                it.copy(
                    email = action.value,
                    emailError = null
                )
            }

            is SignUpAction.OnFullNameChange -> _state.update {
                it.copy(
                    fullName = action.value,
                    fullNameError = null
                )
            }

            is SignUpAction.OnNewsletterCheckChange -> _state.update { it.copy(isNewsletterChecked = action.value) }
            is SignUpAction.OnPasswordChange -> _state.update {
                it.copy(
                    password = action.value,
                    passwordError = null
                )
            }

            SignUpAction.OnSignUpClick -> validateAndSignUp()
            SignUpAction.OnSignInClick -> viewModelScope.launch { _events.send(SignUpEvent.NavigateToSignIn) }
            SignUpAction.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }
    }

    private fun validateAndSignUp() {
        val email = state.value.email
        val password = state.value.password
        val fullName = state.value.fullName

        var hasError = false

        if (fullName.isBlank()) {
            _state.update { it.copy(fullNameError = "Name cannot be empty") }
            hasError = true
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(emailError = "Invalid email address") }
            hasError = true
        }

        if (password.length < 8) {
            _state.update { it.copy(passwordError = "Password must be at least 8 characters") }
            hasError = true
        }

        if (!hasError) {
            signUp()
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            authRepository.signup(state.value.email, state.value.password)
                .onSuccess {
                    runCatching {
                        val guestId = guestTokenManager.getGuestId()
                        cartRepository.mergeGuestCart(guestId)
                        guestTokenManager.clearGuestId()
                    }.fold(
                        onSuccess = {
                            _state.update { it.copy(isLoading = false) }
                            _events.send(SignUpEvent.SignUpSuccess)
                        },
                        onFailure = { mergeError ->
                            val message = mergeError.message ?: "Failed to merge guest cart"
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = DataError.Network.SERVER_ERROR
                                )
                            }
                            _events.send(SignUpEvent.Error(DataError.Network.SERVER_ERROR))
                        }
                    )
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                    _events.send(SignUpEvent.Error(error))
                }
        }
    }
}
