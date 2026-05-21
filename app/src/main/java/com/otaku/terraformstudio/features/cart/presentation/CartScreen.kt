package com.otaku.terraformstudio.features.cart.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.otaku.terraformstudio.R
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.features.cart.domain.Cart
import com.otaku.terraformstudio.features.cart.domain.CartItem
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandLightBeige
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.NotoSerif
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun CartRoot(
    onBackClick: () -> Unit,
    onNavigateToCheckout: (String) -> Unit,
    onNavigateToDiscover: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            CartEvent.NavigateBack -> onBackClick()
            is CartEvent.NavigateToCheckout -> onNavigateToCheckout(event.cartId)
            CartEvent.NavigateToDiscover -> onNavigateToDiscover()
            is CartEvent.NavigateToProduct -> onNavigateToProduct(event.slug)
            is CartEvent.ShowSnackbar -> {}
        }
    }

    CartScreen(state = state, onAction = viewModel::onAction)
}

@Composable
fun CartScreen(
    state: CartState,
    onAction: (CartAction) -> Unit,
) {
    Scaffold(
        containerColor = BrandCream,
    ) { padding ->
        if (state.isLoading && state.cart == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = BrandGold)
            }
        } else if (state.cart != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                CartHeader(onBackClick = { onAction(CartAction.OnBackClick) })

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                ) {
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Your Basket",
                        fontFamily = NotoSerif,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Normal,
                        color = BrandDeepBrown,
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    if (state.cart.items.isEmpty()) {
                        Text(
                            text = "Your basket is empty",
                            fontSize = 15.sp,
                            color = BrandMutedGold,
                            modifier = Modifier.padding(vertical = 40.dp),
                        )
                    } else {
                        CartItemsSection(
                            items = state.cart.items,
                            updatingItemIds = state.updatingItemIds,
                            onIncrease = { onAction(CartAction.OnIncreaseQuantity(it)) },
                            onDecrease = { onAction(CartAction.OnDecreaseQuantity(it)) },
                            onRemove = { onAction(CartAction.OnRemoveItem(it)) },
                            onProductClick = { onAction(CartAction.OnProductClick(it)) }
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    RecommendedSection()
                    Spacer(modifier = Modifier.height(40.dp))

                    OrderSummarySection(cart = state.cart, onAction = onAction)

                }
            }
        }
    }
}

@Composable
private fun CartHeader(onBackClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandCream.copy(alpha = 0.82f))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = BrandGold,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    text = "Your Selection",
                    fontFamily = NotoSerif,
                    fontSize = 20.sp,
                    letterSpacing = (-0.02).sp,
                    color = BrandGold,
                    fontWeight = FontWeight.Normal,
                )
            }
            Text(
                text = "Terra Form",
                fontFamily = NotoSerif,
                fontStyle = FontStyle.Italic,
                fontSize = 18.sp,
                color = BrandGold,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BrandMutedGold.copy(alpha = 0.12f)),
        )
    }
}

@Composable
private fun CartItemsSection(
    items: List<CartItem>,
    updatingItemIds: Set<String>,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onRemove: (String) -> Unit,
    onProductClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(34.dp)) {
        items.forEach { item ->
            CartItemCard(
                modifier = Modifier.clickable {
                    onProductClick(item.slug)
                },
                item = item,
                isUpdating = item.productId in updatingItemIds,
                onIncrease = { onIncrease(item.productId) },
                onDecrease = { onDecrease(item.productId) },
                onRemove = { onRemove(item.productId) },
            )
        }
    }
}

@Composable
private fun CartItemCard(
    modifier: Modifier = Modifier,
    item: CartItem,
    isUpdating: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            modifier = Modifier
                .width(112.dp)
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(8.dp))
                .background(BrandLightBeige),
            contentScale = ContentScale.Crop,
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Row {
                        Text(
                            text = item.technique.uppercase(),
                            fontSize = 10.sp,
                            letterSpacing = 0.16.sp,
                            color = BrandMutedGold,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$${String.format(Locale.US, "%.2f", item.price)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandDeepBrown,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.title,
                        fontFamily = NotoSerif,
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Normal,
                        color = BrandDeepBrown,
                    )
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.description.ifEmpty { item.title },
                fontSize = 13.sp,
                lineHeight = 22.sp,
                color = BrandMutedGold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 190.dp),
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                QuantityStepper(
                    modifier = Modifier.weight(1f),
                    quantity = item.quantity,
                    enabled = !isUpdating,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease,
                )
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = onRemove,
                    enabled = !isUpdating,
                ) {
                    Text(
                        text = "REMOVE",
                        fontSize = 10.sp,
                        letterSpacing = 0.12.sp,
                        color = BrandMutedGold,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    modifier: Modifier = Modifier,
    quantity: Int,
    enabled: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            IconButton(
                onClick = onDecrease,
                enabled = enabled,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_remove),
                    contentDescription = "Remove",
                    tint = BrandGold,
                    modifier = Modifier.size(18.dp),
                )
            }
            Text(
                text = quantity.toString(),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp),
                color = BrandDeepBrown,
            )
            IconButton(
                onClick = onIncrease,
                enabled = enabled,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add",
                    tint = BrandGold,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BrandMutedGold.copy(alpha = 0.3f)),
        )
    }
}

@Composable
private fun RecommendedSection() {
    Column {
        Text(
            text = "You might also like",
            fontFamily = NotoSerif,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            color = BrandDeepBrown,
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun OrderSummarySection(cart: Cart, onAction: (CartAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrandLightBeige)
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text(
            text = "Order Summary",
            fontFamily = NotoSerif,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
            color = BrandDeepBrown,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            SummaryRow(
                label = "Subtotal",
                value = "$${String.format("%.2f", cart.totals.subtotal)}"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Shipping",
                    fontSize = 14.sp,
                    color = BrandMutedGold,
                )
                Text(
                    text = "Calculated at next step",
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    color = BrandMutedGold,
                )
            }

            SummaryRow(
                label = "Estimated Tax",
                value = "$${String.format("%.2f", cart.totals.tax)}",
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .height(1.dp)
                .background(BrandMutedGold.copy(alpha = 0.2f)),
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Total",
                fontFamily = NotoSerif,
                fontSize = 20.sp,
                color = BrandDeepBrown,
            )
            Text(
                text = "$${String.format("%.2f", cart.totals.grandTotal)}",
                fontFamily = NotoSerif,
                fontSize = 26.sp,
                color = BrandDeepBrown,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onAction(CartAction.OnProceedToCheckout) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGold),
        ) {
            Text(
                text = "Proceed to Checkout",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandCream,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { onAction(CartAction.OnContinueShopping) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Continue Shopping",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandGold,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        TrustSection()
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = BrandMutedGold,
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = BrandDeepBrown,
        )
    }
}

@Composable
private fun TrustSection() {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BrandMutedGold.copy(alpha = 0.12f)),
        )
        Spacer(modifier = Modifier.height(10.dp))
        TrustRow(
            iconRes = R.drawable.ic_local_shipping,
            title = "Conscious Delivery",
            description = "Our packaging is 100% plastic-free and recyclable. Most orders ship within 3-5 days.",
        )
        TrustRow(
            iconRes = R.drawable.ic_verified_user,
            title = "Studio Guarantee",
            description = "Broken in transit? We replace any damaged items immediately, no questions asked.",
        )
    }
}

@Composable
private fun TrustRow(iconRes: Int, title: String, description: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = BrandMutedGold,
            modifier = Modifier.size(24.dp),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BrandDeepBrown,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = BrandMutedGold,
            )
        }
    }
}