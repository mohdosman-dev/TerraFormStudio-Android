package com.otaku.terraformstudio.features.auth.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otaku.terraformstudio.core.data.local.GuestTokenManager
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

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val cartRepository: CartRepository,
    private val guestTokenManager: GuestTokenManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignInEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SignInAction) {
        when (action) {
            is SignInAction.OnEmailChange -> _state.update { it.copy(email = action.value, emailError = null) }
            is SignInAction.OnPasswordChange -> _state.update { it.copy(password = action.value, passwordError = null) }
            SignInAction.OnSignInClick -> validateAndSignIn()
            SignInAction.OnSignUpClick -> viewModelScope.launch { _events.send(SignInEvent.NavigateToSignUp) }
            SignInAction.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        }
    }

    private fun validateAndSignIn() {
        val email = state.value.email
        val password = state.value.password

        var hasError = false

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(emailError = "Invalid email address") }
            hasError = true
        }

        if (password.length < 8) {
            _state.update { it.copy(passwordError = "Password must be at least 8 characters") }
            hasError = true
        }

        if (!hasError) {
            signIn()
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            authRepository.login(state.value.email, state.value.password)
                .onSuccess {
                    val guestId = guestTokenManager.getGuestId()
                    cartRepository.mergeGuestCart(guestId)
                    guestTokenManager.clearGuestId()
                    _state.update { it.copy(isLoading = false) }
                    _events.send(SignInEvent.SignInSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error) }
                    _events.send(SignInEvent.Error(error))
                }
        }
    }
}
