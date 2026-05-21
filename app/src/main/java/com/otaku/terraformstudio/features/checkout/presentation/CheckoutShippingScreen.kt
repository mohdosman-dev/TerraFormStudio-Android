package com.otaku.terraformstudio.features.checkout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.otaku.terraformstudio.R
import com.otaku.terraformstudio.features.checkout.domain.DeliveryMethod
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandLightBeige
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import java.util.Locale

@Composable
fun CheckoutShippingScreen(
    state: CheckoutState,
    onAction: (CheckoutAction) -> Unit,
) {
    val address = state.shippingAddress

    Scaffold(
        containerColor = BrandCream,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            CheckoutHeader(onBackClick = { onAction(CheckoutAction.OnBackToCart) })

            StepProgressBar(currentStep = state.currentStep)

            Text(
                text = "Shipping Details",
                fontFamily = NotoSerif,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                letterSpacing = (-0.03).sp,
                fontWeight = FontWeight.Normal,
                color = BrandDeepBrown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {
                GhostTextField(
                    value = address.fullName,
                    onValueChange = {
                        onAction(
                            CheckoutAction.OnShippingAddressChange(
                                address.copy(
                                    fullName = it
                                )
                            )
                        )
                    },
                    label = "Full Name",
                    placeholder = "Elias Thorne",
                )
                GhostTextField(
                    value = address.addressLine1,
                    onValueChange = {
                        onAction(
                            CheckoutAction.OnShippingAddressChange(
                                address.copy(
                                    addressLine1 = it
                                )
                            )
                        )
                    },
                    label = "Shipping Address",
                    placeholder = "Stoneware Lane 42",
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    GhostTextField(
                        value = address.city,
                        onValueChange = {
                            onAction(
                                CheckoutAction.OnShippingAddressChange(
                                    address.copy(
                                        city = it
                                    )
                                )
                            )
                        },
                        label = "City",
                        placeholder = "Portland",
                        modifier = Modifier.weight(1f),
                    )
                    GhostTextField(
                        value = address.postalCode,
                        onValueChange = {
                            onAction(
                                CheckoutAction.OnShippingAddressChange(
                                    address.copy(
                                        postalCode = it
                                    )
                                )
                            )
                        },
                        label = "Zip Code",
                        placeholder = "97201",
                        modifier = Modifier.weight(1f),
                    )
                }
                CountryDropdownField(
                    selectedCountry = address.country,
                    onCountrySelected = { country ->
                        onAction(CheckoutAction.OnShippingAddressChange(address.copy(country = country)))
                    },
                )

                if (state.addressError != null) {
                    Text(
                        text = state.addressError,
                        color = com.otaku.terraformstudio.ui.theme.Error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Delivery Method",
                fontFamily = NotoSerif,
                fontSize = 22.sp,
                color = BrandDeepBrown,
                modifier = Modifier.padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.deliveryMethods.isEmpty() && state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = BrandGold)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.deliveryMethods.forEach { method ->
                        DeliveryMethodCard(
                            method = method,
                            isSelected = method.id == state.selectedDeliveryMethodId,
                            onClick = { onAction(CheckoutAction.OnDeliveryMethodSelect(method.id)) },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onAction(CheckoutAction.OnContinueFromShipping) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = BrandCream,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = "Continue to Payment",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandCream,
                    )
                }
            }

            Text(
                text = "Secure checkout powered by Terra Form. All data is encrypted.",
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = BrandMutedGold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 8.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            CheckoutSummaryCard(state = state)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_local_shipping),
                    contentDescription = null,
                    tint = BrandGold.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = "Complimentary care kit included with every artisanal shipment.",
                    fontSize = 11.sp,
                    letterSpacing = 0.1.sp,
                    lineHeight = 18.sp,
                    color = BrandMutedGold.copy(alpha = 0.6f),
                )
            }

            CheckoutFooter()

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun GhostTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(
        modifier = modifier
            .background(BrandLightBeige, RoundedCornerShape(4.dp))
            .padding(start = 14.dp, end = 14.dp, top = 18.dp, bottom = 8.dp),
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            letterSpacing = 1.4.sp,
            color = BrandMutedGold,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 18.sp,
                    color = BrandMutedGold.copy(alpha = 0.5f),
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = BrandMutedGold.copy(alpha = 0.3f),
                unfocusedIndicatorColor = BrandMutedGold.copy(alpha = 0.15f),
                cursorColor = BrandDeepBrown,
                focusedTextColor = BrandDeepBrown,
                unfocusedTextColor = BrandDeepBrown,
            ),
            textStyle = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = keyboardOptions,
        )
    }
}

@Composable
private fun CountryDropdownField(
    selectedCountry: String,
    onCountrySelected: (String) -> Unit,
) {
    val countries = listOf(
        "United States", "Japan", "Denmark", "United Kingdom",
        "Canada", "Australia", "Germany", "France",
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(BrandLightBeige, RoundedCornerShape(4.dp))
            .padding(start = 14.dp, end = 14.dp, top = 18.dp, bottom = 8.dp),
    ) {
        Text(
            text = "COUNTRY",
            fontSize = 10.sp,
            letterSpacing = 1.4.sp,
            color = BrandMutedGold,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        Box {
            Text(
                text = selectedCountry.ifEmpty { "Select country" },
                fontSize = 18.sp,
                color = if (selectedCountry.isNotEmpty()) BrandDeepBrown else BrandMutedGold.copy(
                    alpha = 0.5f
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(vertical = 8.dp),
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            onCountrySelected(country)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun CheckoutHeader(
    onBackClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandCream)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center,
        ) {
            Text("\u2190", fontSize = 18.sp, color = BrandGold)
        }
        Text(
            text = "The Gallery",
            fontFamily = NotoSerif,
            fontSize = 24.sp,
            letterSpacing = 0.08.sp,
            color = BrandDeepBrown,
            fontWeight = FontWeight.Medium,
        )
        Box(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun DeliveryMethodCard(
    method: DeliveryMethod,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected) BrandLightBeige else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) BrandGold.copy(alpha = 0.3f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable { onClick() }
            .padding(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = method.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandGold,
            )
            Text(
                text = "$${String.format(Locale.US, "%.2f", method.price)}",
                fontSize = 13.sp,
                color = BrandMutedGold,
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = method.description,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            color = BrandMutedGold.copy(alpha = 0.8f),
        )
    }
}

@Composable
internal fun CheckoutSummaryCard(state: CheckoutState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(BrandLightBeige, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (state.items.isEmpty())
                Text(
                    text = "Studio Still Life",
                    fontSize = 12.sp,
                    color = BrandMutedGold.copy(alpha = 0.5f),
                )
            else
                AsyncImage(
                    model = state.items.firstOrNull()?.imageUrl,
                    contentDescription = state.items.firstOrNull()?.slug,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandLightBeige, RoundedCornerShape(16.dp))
                .padding(20.dp),
        ) {
            Text(
                text = "Order Summary",
                fontFamily = NotoSerif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                color = BrandDeepBrown,
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (state.session != null) {
                val summary = state.session.priceSummary
                SummaryLine("Subtotal", "$${String.format(Locale.US, "%.2f", summary.subtotal)}")
                Spacer(modifier = Modifier.height(14.dp))
                SummaryLine("Shipping", "Calculated next")
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BrandMutedGold.copy(alpha = 0.12f)),
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (state.session != null) {
                val summary = state.session.priceSummary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = "Total",
                        fontFamily = NotoSerif,
                        fontSize = 18.sp,
                        color = BrandDeepBrown,
                    )
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", summary.grandTotal)}",
                        fontFamily = NotoSerif,
                        fontSize = 28.sp,
                        color = BrandDeepBrown,
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = BrandMutedGold,
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandDeepBrown,
        )
    }
}

@Composable
internal fun CheckoutFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Terra Form Studio",
            fontFamily = NotoSerif,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontSize = 18.sp,
            color = BrandMutedGold.copy(alpha = 0.4f),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                "Privacy",
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = BrandMutedGold.copy(alpha = 0.7f)
            )
            Text(
                "Terms",
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = BrandMutedGold.copy(alpha = 0.7f)
            )
            Text(
                "Returns",
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                color = BrandMutedGold.copy(alpha = 0.7f)
            )
        }
    }
}
