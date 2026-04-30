package com.otaku.terraformstudio.features.product.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.core.presentation.components.ProductCard
import com.otaku.terraformstudio.features.product.domain.ProductDetail
import com.otaku.terraformstudio.features.product.domain.ProductSpecifications
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
            is ProductDetailEvent.ShowSnackbar -> {
                // Launch snackbar
            }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            state.product?.let { product ->
                ProductBottomBar(product = product, onAddToCart = { onAction(ProductDetailAction.OnAddToCart) })
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.product != null) {
            val product = state.product
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = padding.calculateBottomPadding())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ProductImageHeader(
                    media = product.media,
                    onBackClick = { onAction(ProductDetailAction.OnBackClick) }
                )
                
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = product.artisan.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 32.sp,
                        lineHeight = 40.sp
                    )
                    product.subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "The Studio Story",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = NotoSerif
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = product.descriptionLong,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 26.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(40.dp))
                    
                    SpecificationsSection(specs = product.specifications)
                    
                    if (product.relatedProducts.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            text = "You May Also Like",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        product.relatedProducts.forEach { related ->
                            // Reusing ProductCard but in a list or horizontal row
                            ProductCard(
                                id = related.id,
                                title = related.title,
                                price = related.price,
                                imageUrl = related.imageUrl,
                                onClick = { onAction(ProductDetailAction.OnRelatedProductClick(related.id, related.id)) }, // Needs slug
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductImageHeader(
    media: List<com.otaku.terraformstudio.features.product.domain.ProductMedia>,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { media.size })
    
    Box(modifier = Modifier.fillMaxWidth().height(480.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = media[page].url,
                contentDescription = media[page].alt,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        
        // Pager Indicator
        if (media.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(media.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color)
                            .size(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SpecificationsSection(specs: ProductSpecifications) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Technical Specifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                SpecItem("Material", specs.material)
                SpecItem("Technique", specs.technique)
                SpecItem("Glaze", specs.glaze)
                specs.dimensions?.let { SpecItem("Dimensions", it) }
                SpecItem("Care", specs.care)
            }
        }
    }
}

@Composable
fun SpecItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProductBottomBar(product: ProductDetail, onAddToCart: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .height(56.dp)
                    .width(200.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Add to Cart", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
    }
}
