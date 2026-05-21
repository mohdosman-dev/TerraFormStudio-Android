package com.otaku.terraformstudio.features.checkout.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckoutRoot(
    cartId: String,
    onBackClick: () -> Unit,
    onOrderPlaced: (orderId: String, grandTotal: Double) -> Unit,
    viewModel: CheckoutViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(cartId) {
        viewModel.initialize(cartId)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CheckoutEvent.NavigateBack -> onBackClick()
            is CheckoutEvent.OrderPlaced -> onOrderPlaced(event.orderId, event.grandTotal)
            is CheckoutEvent.ShowError -> { }
        }
    }

    when (state.currentStep) {
        CheckoutStep.SHIPPING -> CheckoutShippingScreen(
            state = state,
            onAction = viewModel::onAction,
        )
        CheckoutStep.PAYMENT -> CheckoutPaymentScreen(
            state = state,
            onAction = viewModel::onAction,
        )
        CheckoutStep.REVIEW -> CheckoutReviewScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@Composable
fun OrderConfirmationRoot(
    orderId: String,
    grandTotal: Double,
    onContinueShopping: () -> Unit,
) {
    OrderConfirmationScreen(
        confirmation = com.otaku.terraformstudio.features.checkout.domain.OrderConfirmation(
            orderId = orderId,
            status = "confirmed",
            grandTotal = grandTotal,
        ),
        onContinueShopping = onContinueShopping,
    )
}
