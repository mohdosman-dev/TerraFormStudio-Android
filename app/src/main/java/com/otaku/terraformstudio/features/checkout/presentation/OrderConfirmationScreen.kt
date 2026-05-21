package com.otaku.terraformstudio.features.checkout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otaku.terraformstudio.features.checkout.domain.OrderConfirmation
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandLightBeige
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import java.util.Locale

@Composable
fun OrderConfirmationScreen(
    confirmation: OrderConfirmation?,
    onContinueShopping: () -> Unit,
) {
    Scaffold(
        containerColor = BrandCream,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            CheckoutHeader(onBackClick = { })

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(BrandLightBeige, RoundedCornerShape(999.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("\u2713", fontSize = 32.sp, color = BrandGold, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = if (confirmation?.customerName.isNullOrEmpty()) "Thank you.\nYour piece is on its way."
                    else "Thank you, ${confirmation!!.customerName}.\nYour piece is on its way.",
                    fontFamily = NotoSerif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    fontSize = 38.sp,
                    lineHeight = 44.sp,
                    textAlign = TextAlign.Center,
                    color = BrandDeepBrown,
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Order Confirmed",
                    fontSize = 12.sp,
                    letterSpacing = 1.4.sp,
                    color = BrandMutedGold,
                )

                if (confirmation != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "#${confirmation.orderNumber.ifEmpty { confirmation.orderId.take(6).uppercase() }}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandGold,
                    )
                }
            }

            // Order Summary Card
            if (confirmation != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(BrandLightBeige, RoundedCornerShape(20.dp))
                        .border(1.dp, BrandMutedGold.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                        .padding(20.dp),
                ) {
                    Text(
                        "Order Summary",
                        fontFamily = NotoSerif,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-0.02).sp,
                        color = BrandDeepBrown,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (confirmation.items.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            confirmation.items.forEach { item ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(74.dp)
                                            .height(98.dp)
                                            .background(BrandLightBeige, RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(item.title.take(2), fontSize = 12.sp, color = BrandMutedGold)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            item.title,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrandDeepBrown,
                                            lineHeight = 22.sp,
                                        )
                                        Text(
                                            "$${String.format(Locale.US, "%.2f", item.price)}",
                                            fontSize = 13.sp,
                                            color = BrandMutedGold,
                                            modifier = Modifier.padding(top = 6.dp),
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(BrandMutedGold.copy(alpha = 0.15f)),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ConfirmationSummaryLine("Subtotal", "$${String.format(Locale.US, "%.2f", confirmation.grandTotal * 0.8)}")
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Shipping", fontSize = 14.sp, fontWeight = FontWeight.Light, color = BrandMutedGold)
                        Text(
                            "Complimentary",
                            fontSize = 10.sp,
                            letterSpacing = 1.4.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandGold,
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(BrandMutedGold.copy(alpha = 0.2f)),
                    )
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Text("Total", fontSize = 18.sp, fontWeight = FontWeight.Light, color = BrandDeepBrown)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("USD", fontSize = 11.sp, letterSpacing = 1.4.sp, color = BrandMutedGold)
                            Text(
                                "$${String.format(Locale.US, "%.2f", confirmation.grandTotal)}",
                                fontFamily = NotoSerif,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (-0.03).sp,
                                color = BrandDeepBrown,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Logistics
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandLightBeige, RoundedCornerShape(10.dp))
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                Text(
                                    "Expected arrival",
                                    fontSize = 10.sp,
                                    letterSpacing = 1.4.sp,
                                    color = BrandMutedGold,
                                )
                                Text(
                                    "${confirmation.expectedDeliveryStart.ifEmpty { "Oct 24" }} - ${confirmation.expectedDeliveryEnd.ifEmpty { "Oct 27" }}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandDeepBrown,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                        }
                        if (confirmation.shippingAddress != null) {
                            Column {
                                Text(
                                    "Shipping to",
                                    fontSize = 10.sp,
                                    letterSpacing = 1.4.sp,
                                    color = BrandMutedGold,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                val addr = confirmation.shippingAddress
                                Text(
                                    "${addr.fullName}\n${addr.addressLine1}${if (addr.addressLine2.isNotEmpty()) ", ${addr.addressLine2}" else ""}\n${addr.city}, ${addr.state} ${addr.postalCode}",
                                    fontSize = 13.sp,
                                    lineHeight = 22.sp,
                                    color = BrandMutedGold,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                    ) {
                        Text("Track Order", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = BrandCream)
                    }
                    Button(
                        onClick = onContinueShopping,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Text(
                            "Continue Shopping",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp,
                            color = BrandGold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(BrandLightBeige, RoundedCornerShape(10.dp))
                        .padding(20.dp),
                ) {
                    Text(
                        text = "\"Each Terra Form piece is packed with biodegradable cellulose and recycled wood wool to ensure its safe journey from our kiln to your home.\"",
                        fontSize = 13.sp,
                        lineHeight = 23.sp,
                        color = BrandMutedGold,
                        fontStyle = FontStyle.Italic,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bottom Nav
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(BrandLightBeige, RoundedCornerShape(999.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    listOf("\u2302", "\uD83D\uDD0D", "\uD83D\uDECD", "\uD83D\uDC64").forEachIndexed { index, icon ->
                        val isSelected = index == 3
                        Text(
                            icon,
                            fontSize = if (isSelected) 24.sp else 20.sp,
                            color = if (isSelected) BrandGold else BrandMutedGold.copy(alpha = 0.4f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ConfirmationSummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Light, color = BrandMutedGold)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = BrandDeepBrown)
    }
}
