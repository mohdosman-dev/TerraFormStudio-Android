package com.otaku.terraformstudio.features.auth.presentation.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.core.presentation.components.TerraPrimaryButton
import com.otaku.terraformstudio.core.presentation.components.TerraTextField
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInRoot(
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: SignInViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignInEvent.Error -> { /* Handle error toast */
            }

            SignInEvent.NavigateToSignUp -> onNavigateToSignUp()
            SignInEvent.SignInSuccess -> onSignInSuccess()
        }
    }

    SignInScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SignInScreen(
    state: SignInState,
    onAction: (SignInAction) -> Unit
) {
    Scaffold(
        containerColor = BrandCream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 64.dp, top = 32.dp)
            ) {
                Text(
                    text = "Terra Form Studio",
                    fontFamily = NotoSerif,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp,
                    color = BrandGold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(1.dp)
                        .clip(RoundedCornerShape(1.dp))
                )
            }

            // Copy
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Welcome Back",
                    fontFamily = NotoSerif,
                    fontSize = 34.sp,
                    lineHeight = 38.sp,
                    color = BrandDeepBrown
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Sign in to continue your collection journey.",
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    color = BrandMutedGold
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TerraTextField(
                    value = state.email,
                    onValueChange = { onAction(SignInAction.OnEmailChange(it)) },
                    label = "Email Address",
                    placeholder = "hello@example.com",
                    isError = state.emailError != null,
                    errorMessage = state.emailError
                )

                TerraTextField(
                    value = state.password,
                    onValueChange = { onAction(SignInAction.OnPasswordChange(it)) },
                    label = "Password",
                    placeholder = "••••••••",
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { onAction(SignInAction.OnTogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = BrandMutedGold.copy(alpha = 0.7f)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TerraPrimaryButton(
                    text = "Sign In",
                    onClick = { onAction(SignInAction.OnSignInClick) },
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = state.isLoading,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "New collector?",
                        fontSize = 14.sp,
                        color = BrandMutedGold,
                    )
                    Text(
                        text = "Create Account",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandGold,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable {
                                onAction(SignInAction.OnSignUpClick)
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        state = SignInState(
            email = "test@example.com",
            password = "password"
        ),
        onAction = {}
    )
}
