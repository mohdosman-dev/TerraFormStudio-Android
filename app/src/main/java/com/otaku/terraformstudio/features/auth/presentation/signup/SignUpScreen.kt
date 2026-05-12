package com.otaku.terraformstudio.features.auth.presentation.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
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
fun SignUpRoot(
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SignUpEvent.Error -> { /* Handle error toast */
            }

            SignUpEvent.NavigateToSignIn -> onNavigateToSignIn()
            SignUpEvent.SignUpSuccess -> onSignUpSuccess()
        }
    }

    SignUpScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit
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
                modifier = Modifier.padding(bottom = 32.dp)
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
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(1.dp))
                )
            }

            // Hero Image
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAi9jVcQSEeR03Pv75DGD0ss5hey07zwEmoze59QAedc9NNjx0yNjpbbbiSt9oEUZtR5CE3_xJPXXxu5ZxFu6kzufRTe05I9SQ9dUr3JPcjuax2zgUfeKcG7Qai6mM7e4aNdBZlGo5gc_u-hKQ7Vh2aPDZvWl4r2C5sdlZlN27h75Js6ckBxPR6S3pnON6BmxG9DS8FkRQ3mUs3wKnviiUOQqfhGBzN-jboMP173in-Nu79p-gECWQ42yjjTDT35tjbWIn2XR-XEbk",
                contentDescription = "Ceramic Process",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Copy
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Join the Collector Community",
                    fontFamily = NotoSerif,
                    fontSize = 34.sp,
                    lineHeight = 38.sp,
                    color = BrandDeepBrown
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Step into our studio space. Be the first to witness the transformation of earth to art.",
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    color = BrandMutedGold,
                    modifier = Modifier.width(280.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Form
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TerraTextField(
                    value = state.fullName,
                    onValueChange = { onAction(SignUpAction.OnFullNameChange(it)) },
                    label = "Full Name",
                    placeholder = "Enter your name",
                    isError = state.fullNameError != null,
                    errorMessage = state.fullNameError
                )

                TerraTextField(
                    value = state.email,
                    onValueChange = { onAction(SignUpAction.OnEmailChange(it)) },
                    label = "Email Address",
                    placeholder = "hello@example.com",
                    isError = state.emailError != null,
                    errorMessage = state.emailError
                )

                TerraTextField(
                    value = state.password,
                    onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
                    label = "Password",
                    placeholder = "••••••••",
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { onAction(SignUpAction.OnTogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = BrandMutedGold.copy(alpha = 0.7f)
                            )
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = state.isNewsletterChecked,
                        onCheckedChange = { onAction(SignUpAction.OnNewsletterCheckChange(it)) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BrandGold,
                            uncheckedColor = BrandMutedGold.copy(alpha = 0.2f)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Join our newsletter for kiln firings and artisan stories.",
                        fontSize = 14.sp,
                        lineHeight = 24.sp,
                        color = BrandMutedGold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TerraPrimaryButton(
                    text = "Create Account",
                    onClick = { onAction(SignUpAction.OnSignUpClick) },
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
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account?",
                        fontSize = 14.sp,
                        color = BrandMutedGold
                    )
                    Text(
                        text = "Sign In",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandGold,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable {
                                onAction(SignUpAction.OnSignInClick)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        state = SignUpState(),
    ) { }
}