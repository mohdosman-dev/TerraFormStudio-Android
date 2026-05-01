package com.otaku.terraformstudio.features.product.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.core.presentation.components.ProductCard
import com.otaku.terraformstudio.features.product.domain.ProductDetail
import com.otaku.terraformstudio.ui.theme.NotoSerif
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailRoot(
    slug: String,
    onBackClick: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel(parameters = { parametersOf(slug) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ProductDetailEvent.ShowSnackbar -> { /* Implement snackbar show */ }
            is ProductDetailEvent.NavigateToProduct -> onNavigateToProduct(event.slug)
            ProductDetailEvent.NavigateBack -> onBackClick()
        }
    }

    ProductDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onAction: (ProductDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        containerColor = Color(0xFFFAF9F6),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "The Gallery",
                        fontFamily = NotoSerif,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF2F2A24)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ProductDetailAction.OnBackClick) }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF7A6A53))
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Cart */ }) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = "Cart", tint = Color(0xFF7A6A53))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF9F6).copy(alpha = 0.90f)
                )
            )
        },
        bottomBar = {
            AppBottomNavigation()
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7A6A53))
            }
        } else if (state.product != null) {
            val product = state.product
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                // Product Image Card
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        AsyncImage(
                            model = product.media.firstOrNull()?.url,
                            contentDescription = product.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.85f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Info Section
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                    Text(
                        text = "ARCHIVE · VASES",
                        fontSize = 10.sp,
                        color = Color(0xFFA29689),
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = product.title,
                        fontFamily = NotoSerif,
                        fontSize = 40.sp,
                        lineHeight = 41.sp,
                        color = Color(0xFF2F2A24),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "$${String.format("%.2f", product.price)}",
                        fontSize = 24.sp,
                        color = Color(0xFFA68A78),
                        modifier = Modifier.padding(bottom = 18.dp)
                    )
                    Text(
                        text = product.descriptionLong,
                        fontSize = 15.sp,
                        lineHeight = 27.sp,
                        color = Color(0xFF7F7468)
                    )
                }

                // Specifications Grid Card
                Card(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFF7A6A53).copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SpecItem(label = "Dimensions", value = product.specifications.dimensions ?: "8\" × 5\"")
                        SpecItem(label = "Material", value = product.specifications.material)
                        SpecItem(label = "Care", value = product.specifications.care)
                    }
                }

                // Actions
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Button(
                        onClick = { onAction(ProductDetailAction.OnAddToCart) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A6A53))
                    ) {
                        Text(text = "Add to Bag", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WISHLIST", 
                            fontSize = 11.sp, 
                            letterSpacing = 1.sp, 
                            color = Color(0xFF7F7468),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = "SHARE", 
                            fontSize = 11.sp, 
                            letterSpacing = 1.sp, 
                            color = Color(0xFF7F7468),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Artisan Section
                Card(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EDE5))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "MEET THE ARTISAN",
                            fontSize = 10.sp,
                            color = Color(0xFFA29689),
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = product.artisan.studioImageUrl,
                                contentDescription = product.artisan.name,
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .border(5.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Crafted by ${product.artisan.name}",
                                    fontFamily = NotoSerif,
                                    fontSize = 24.sp,
                                    lineHeight = 30.sp,
                                    color = Color(0xFF2F2A24)
                                )
                                Text(
                                    text = product.artisan.philosophy ?: "",
                                    fontSize = 13.sp,
                                    lineHeight = 22.sp,
                                    color = Color(0xFF7F7468)
                                )
                            }
                        }
                    }
                }

                // Recommendations
                if (product.relatedProducts.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp).padding(bottom = 100.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "You May Also Like", 
                                fontFamily = NotoSerif, 
                                fontSize = 28.sp,
                                color = Color(0xFF2F2A24)
                            )
                            Text(
                                text = "SEE ALL", 
                                fontSize = 10.sp, 
                                color = Color(0xFFA29689), 
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        val chunked = product.relatedProducts.chunked(2)
                        chunked.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(), 
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                row.forEach { related ->
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(top = if (index > 0 && row.indexOf(related) == 1) 22.dp else 0.dp)
                                    ) {
                                        ProductCard(
                                            id = related.id,
                                            title = related.title,
                                            price = related.price,
                                            imageUrl = related.imageUrl,
                                            onClick = { onAction(ProductDetailAction.OnRelatedProductClick(related.id, related.id)) }
                                        )
                                    }
                                }
                                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecItem(label: String, value: String) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            color = Color(0xFFA29689),
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2F2A24)
        )
    }
}

@Composable
private fun AppBottomNavigation() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color(0xFF7A6A53).copy(alpha = 0.08f)),
        color = Color(0xFFFAF9F6).copy(alpha = 0.96f)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .navigationBarsPadding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(label = "Curated", icon = "▦", isSelected = false)
            NavItem(label = "Studio", icon = "☰", isSelected = false)
            NavItem(label = "Archive", icon = "✎", isSelected = true)
            NavItem(label = "Cart", icon = "👜", isSelected = false)
        }
    }
}

@Composable
private fun NavItem(label: String, icon: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = icon, 
            fontSize = 18.sp, 
            color = if (isSelected) Color(0xFF7A6A53) else Color(0xFF7F7468)
        )
        Text(
            text = label, 
            fontSize = 11.sp, 
            color = if (isSelected) Color(0xFF7A6A53) else Color(0xFF7F7468)
        )
    }
}
