package com.otaku.terraformstudio.features.checkout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandLightBeige
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import java.util.Locale

@Composable
fun CheckoutPaymentScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
) {
    var billingSameAsShipping by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = BrandCream,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            CheckoutHeader(onBackClick = { onAction(CheckoutAction.OnBackToShipping) })

            StepProgressBar(currentStep = state.currentStep)

            Text(
                text = "Payment Details",
                fontFamily = NotoSerif,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                letterSpacing = (-0.03).sp,
                fontWeight = FontWeight.Bold,
                color = BrandDeepBrown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(34.dp),
            ) {
                Column {
                    Text(
                        text = "Express Checkout",
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        color = BrandMutedGold.copy(alpha = 0.7f),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandLightBeige),
                        ) {
                            Text(
                                "Apple Pay",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = BrandDeepBrown
                            )
                        }
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandLightBeige),
                        ) {
                            Text(
                                "PayPal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF003087),
                            )
                        }
                    }
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Credit Card Information",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            color = BrandMutedGold.copy(alpha = 0.7f),
                        )
                        Text(
                            text = "\uD83D\uDCB3",
                            fontSize = 18.sp,
                            color = BrandMutedGold.copy(alpha = 0.7f),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                        ) {
                            CreditCardField(
                                value = state.cardNumber,
                                onValueChange = { onAction(CheckoutAction.OnCardNumberChange(it)) },
                                label = "Card Number",
                                placeholder = "0000 0000 0000 0000",
                                keyboardType = KeyboardType.Number,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CreditCardField(
                                    value = state.cardExpiry,
                                    onValueChange = { onAction(CheckoutAction.OnCardExpiryChange(it)) },
                                    label = "Expiry Date",
                                    placeholder = "MM / YY",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f),
                                )
                                CreditCardField(
                                    value = state.cardCvv,
                                    onValueChange = { onAction(CheckoutAction.OnCardCvvChange(it)) },
                                    label = "CVV",
                                    placeholder = "000",
                                    keyboardType = KeyboardType.Number,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BrandLightBeige, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Billing address same as shipping",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandDeepBrown,
                        )
                        Text(
                            text = "${state.shippingAddress.city}, ${state.shippingAddress.state}",
                            fontSize = 12.sp,
                            color = BrandMutedGold.copy(alpha = 0.7f),
                        )
                    }
                    Switch(
                        checked = billingSameAsShipping,
                        onCheckedChange = { billingSameAsShipping = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = BrandGold,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = BrandMutedGold.copy(alpha = 0.3f),
                        ),
                    )
                }

                Button(
                    onClick = { onAction(CheckoutAction.OnContinueFromPayment) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                    enabled = state.cardNumber.length >= 13 && state.cardExpiry.length >= 5,
                ) {
                    Text(
                        text = "Review Order",
                        fontSize = 13.sp,
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandCream,
                    )
                }

                PaymentSummaryCard(state = state)
            }

            Spacer(modifier = Modifier.height(20.dp))

            CheckoutFooter()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CreditCardField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = BrandMutedGold.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 6.dp),
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder, fontSize = 14.sp, color = BrandMutedGold.copy(alpha = 0.5f))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BrandLightBeige,
                unfocusedContainerColor = BrandLightBeige,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = BrandDeepBrown,
                focusedTextColor = BrandDeepBrown,
                unfocusedTextColor = BrandDeepBrown,
            ),
            textStyle = TextStyle(fontSize = 14.sp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

@Composable
private fun PaymentSummaryCard(state: CheckoutState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandLightBeige, RoundedCornerShape(16.dp))
            .border(1.dp, BrandMutedGold.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Order Summary",
            fontFamily = NotoSerif,
            fontSize = 26.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = (-0.02).sp,
            color = BrandDeepBrown,
        )

        if (state.items.isNotEmpty()) {
            state.items.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                BrandMutedGold.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(item.title.take(2), fontSize = 10.sp, color = BrandMutedGold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            item.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = BrandDeepBrown
                        )
                        if (item.variant.isNotEmpty()) {
                            Text(
                                item.variant,
                                fontSize = 12.sp,
                                color = BrandMutedGold.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            "$${String.format(Locale.US, "%.2f", item.price)}",
                            fontSize = 14.sp,
                            color = BrandDeepBrown,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
        }

        if (state.session != null) {
            val summary = state.session.priceSummary
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(BrandMutedGold.copy(alpha = 0.2f)),
                )
                SummaryLine("Subtotal", "$${String.format(Locale.US, "%.2f", summary.subtotal)}")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Shipping",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        color = BrandMutedGold
                    )
                    Text(
                        "Free",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandGold,
                        fontStyle = FontStyle.Italic,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        "Total",
                        fontFamily = NotoSerif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = BrandDeepBrown,
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "USD",
                            fontSize = 10.sp,
                            letterSpacing = 1.4.sp,
                            color = BrandMutedGold
                        )
                        Text(
                            "$${String.format(Locale.US, "%.2f", summary.grandTotal)}",
                            fontFamily = NotoSerif,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.03).sp,
                            color = BrandDeepBrown,
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandCream, RoundedCornerShape(10.dp))
                .border(1.dp, BrandMutedGold.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                .padding(14.dp),
        ) {
            Text(
                text = "\u201CEach piece is hand-thrown and kiln-fired in our Ojai studio. Small variations in glaze and form are celebrated as marks of the maker\u2019s hand.\u201D",
                fontSize = 11.sp,
                lineHeight = 19.sp,
                color = BrandMutedGold,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Light, color = BrandMutedGold)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown)
    }
}
