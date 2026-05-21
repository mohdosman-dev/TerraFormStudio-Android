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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otaku.terraformstudio.features.checkout.domain.CheckoutItem
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandLightBeige
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import java.util.Locale

@Composable
fun CheckoutReviewScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
) {
    val session = state.session

    Scaffold(
        containerColor = BrandCream,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            CheckoutHeader(onBackClick = { onAction(CheckoutAction.OnBackToPayment) })

            StepProgressBar(currentStep = state.currentStep)

            Text(
                text = "Order Review",
                fontFamily = NotoSerif,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                letterSpacing = (-0.03).sp,
                fontWeight = FontWeight.Bold,
                color = BrandDeepBrown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (state.items.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    state.items.forEach { item ->
                        ReviewItemCard(item = item)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (session != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Shipping Address
                    ReviewInfoCard(
                        title = "Shipping Address",
                        onEdit = { onAction(CheckoutAction.OnBackToShipping) },
                    ) {
                        val addr = session.shippingAddress
                        if (addr != null) {
                            Text(addr.fullName, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "${addr.addressLine1}${if (addr.addressLine2.isNotEmpty()) ", ${addr.addressLine2}" else ""}",
                                fontSize = 13.sp, lineHeight = 22.sp, color = BrandMutedGold, fontWeight = FontWeight.Light,
                            )
                            Text("${addr.city}, ${addr.state} ${addr.postalCode}", fontSize = 13.sp, color = BrandMutedGold, fontWeight = FontWeight.Light)
                            Text(addr.country, fontSize = 13.sp, color = BrandMutedGold, fontWeight = FontWeight.Light)
                            if (addr.phone.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(addr.phone, fontSize = 13.sp, color = BrandGold)
                            }
                        }
                    }

                    // Payment Method
                    ReviewInfoCard(
                        title = "Payment Method",
                        onEdit = { onAction(CheckoutAction.OnBackToPayment) },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("\uD83D\uDCB3", fontSize = 16.sp)
                            Text(
                                "Visa ending in ${state.cardNumber.takeLast(4).ifEmpty { "4242" }}",
                                fontSize = 14.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Expires ${state.cardExpiry.ifEmpty { "11/26" }}\nBilling address matches shipping",
                            fontSize = 13.sp, lineHeight = 22.sp, color = BrandMutedGold, fontWeight = FontWeight.Light,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Order Summary
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandLightBeige),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Order Summary",
                        fontFamily = NotoSerif,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp,
                        color = BrandDeepBrown,
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    if (session != null) {
                        val summary = session.priceSummary
                        RowLine("Subtotal", "$${String.format(Locale.US, "%.2f", summary.subtotal)}")
                        Spacer(modifier = Modifier.height(14.dp))
                        RowLine("Shipping (Eco-Parcel)", "$${String.format(Locale.US, "%.2f", summary.shipping)}")
                        Spacer(modifier = Modifier.height(14.dp))
                        RowLine("Tax (Estimated)", "$${String.format(Locale.US, "%.2f", summary.tax)}")
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(BrandMutedGold.copy(alpha = 0.2f)),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            Text(
                                "Total",
                                fontFamily = NotoSerif,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandDeepBrown,
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text("USD", fontSize = 10.sp, letterSpacing = 1.4.sp, color = BrandMutedGold)
                                Text(
                                    "$${String.format(Locale.US, "%.2f", summary.grandTotal)}",
                                    fontFamily = NotoSerif,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = (-0.03).sp,
                                    color = BrandDeepBrown,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { onAction(CheckoutAction.OnPlaceOrder) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                        enabled = !state.isPlacingOrder,
                    ) {
                        if (state.isPlacingOrder) {
                            CircularProgressIndicator(color = BrandCream, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                "Place Order",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp,
                                color = BrandCream,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        "By placing this order you agree to our Terms of Service & Return Policy",
                        fontSize = 10.sp,
                        lineHeight = 17.sp,
                        textAlign = TextAlign.Center,
                        color = BrandMutedGold,
                        letterSpacing = 1.2.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text("\u2705", fontSize = 16.sp)
                Text(
                    "Each ceramic piece is double-inspected for structural integrity and carefully packaged in biodegradable padding.",
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 1.5.sp,
                    color = BrandMutedGold.copy(alpha = 0.5f),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            CheckoutBottomNav()

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ReviewItemCard(item: CheckoutItem) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(120.dp)
                .background(BrandLightBeige, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(item.title.take(2), fontSize = 14.sp, color = BrandMutedGold)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(item.title, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown)
                if (item.variant.isNotEmpty()) {
                    Text(item.variant, fontSize = 13.sp, color = BrandMutedGold, fontWeight = FontWeight.Light)
                }
                if (item.tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        item.tags.forEach { tag ->
                            Text(
                                tag.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color(0xFF644D3D),
                                modifier = Modifier
                                    .background(Color(0xFFFDDCC7), RoundedCornerShape(999.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text("Qty: ${item.quantity}", fontSize = 13.sp, color = BrandMutedGold, fontStyle = FontStyle.Italic)
                Text(
                    "$${String.format(Locale.US, "%.2f", item.price * item.quantity)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = BrandDeepBrown,
                )
            }
        }
    }
}

@Composable
private fun ReviewInfoCard(
    title: String,
    onEdit: () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BrandLightBeige),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(title, fontSize = 11.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.SemiBold, color = BrandMutedGold)
                TextButton(onClick = onEdit) {
                    Text("Edit", fontSize = 12.sp, color = BrandGold)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            content()
        }
    }
}

@Composable
private fun RowLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 14.sp, color = BrandMutedGold)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown)
    }
}

@Composable
private fun CheckoutBottomNav() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandCream.copy(alpha = 0.8f))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf("\u2302", "\uD83D\uDD0D", "\uD83D\uDECD", "\uD83D\uDC64").forEachIndexed { index, icon ->
            val isSelected = index == 2
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        if (isSelected) BrandLightBeige else Color.Transparent,
                        RoundedCornerShape(999.dp),
                    )
                    .padding(12.dp),
            ) {
                Text(icon, fontSize = 20.sp, color = if (isSelected) BrandGold else BrandMutedGold.copy(alpha = 0.4f))
            }
        }
    }
}
