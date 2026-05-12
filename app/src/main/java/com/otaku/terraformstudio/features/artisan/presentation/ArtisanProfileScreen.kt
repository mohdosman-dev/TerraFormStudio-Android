package com.otaku.terraformstudio.features.artisan.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProduct
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProfile
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandSand
import com.otaku.terraformstudio.ui.theme.NotoSerif
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ArtisanProfileRoot(
    slug: String,
    onBackClick: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    viewModel: ArtisanProfileViewModel = koinViewModel(parameters = { parametersOf(slug) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ArtisanProfileEvent.NavigateBack -> onBackClick()
            is ArtisanProfileEvent.NavigateToProduct -> onNavigateToProduct(event.slug)
        }
    }

    ArtisanProfileScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtisanProfileScreen(
    state: ArtisanProfileState,
    onAction: (ArtisanProfileAction) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFFAF9F6),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "The Tactile Gallery",
                        fontFamily = NotoSerif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF7A6A53)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ArtisanProfileAction.OnBackClick) }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Menu",
                            tint = Color(0xFF7A6A53)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF9F6).copy(alpha = 0.90f)
                )
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7A6A53))
            }
        } else {
            val error = state.error
            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error.asString(),
                        color = Color(0xFF7F7468),
                        fontSize = 15.sp
                    )
                }
            } else if (state.profile != null) {
                val profile = state.profile
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                ) {
                    HeroSection(profile = profile)
                    PortraitAndNameSection(profile = profile)
                    PhilosophySection(profile = profile)
                    StudioDetailsSection(profile = profile)
                    AvailableWorksSection(
                        products = profile.products,
                        onProductClick = { onAction(ArtisanProfileAction.OnProductClick(it)) }
                    )
                    //NewsletterSection()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun HeroSection(profile: ArtisanProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        AsyncImage(
            model = profile.heroImage?.realUrl,
            contentDescription = profile.heroImage?.alt ?: profile.displayName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (profile.heroImage == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE7DFD5))
            )
        }
    }
}

@Composable
private fun PortraitAndNameSection(profile: ArtisanProfile) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Column(
            modifier = Modifier.offset(y = (-40).dp),
            horizontalAlignment = Alignment.End
        ) {
            Box(
                modifier = Modifier
                    .width(108.dp)
                    .height(138.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .align(Alignment.End)
            ) {
                AsyncImage(
                    model = profile.heroImage?.realUrl,
                    contentDescription = profile.displayName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (profile.heroImage == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE7DFD5))
                    )
                }
            }
            Column(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(
                    text = profile.displayName,
                    fontFamily = NotoSerif,
                    fontSize = 42.sp,
                    lineHeight = 40.sp,
                    color = Color(0xFF7A6A53),
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    profile.studioStory?.techniques?.firstOrNull()?.let {
                        InfoChip(text = it)
                    }
                    profile.location?.let { loc ->
                        val locationStr = listOfNotNull(loc.city, loc.country).joinToString(", ")
                        if (locationStr.isNotBlank()) {
                            InfoChip(text = locationStr)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF7A6A53),
        letterSpacing = 1.2.sp,
        modifier = Modifier
            .background(
                color = Color(0xFFEEE7DD),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun PhilosophySection(profile: ArtisanProfile) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        profile.studioStory?.philosophy?.let { philosophy ->
            Text(
                text = philosophy,
                fontFamily = NotoSerif,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                color = Color(0xFF2F2A24),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        profile.bioLong?.let { bio ->
            Text(
                text = bio,
                fontSize = 14.sp,
                lineHeight = 26.sp,
                color = Color(0xFF7F7468)
            )
        }
    }
}

@Composable
private fun StudioDetailsSection(profile: ArtisanProfile) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EDE5)),
        border = BorderStroke(1.dp, Color(0xFF7A6A53).copy(alpha = 0.10f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            profile.studioStory?.materials?.let { materials ->
                if (materials.isNotEmpty()) {
                    StudioDetailItem(
                        label = "The Material",
                        value = materials.joinToString(", ")
                    )
                }
            }
            profile.studioStory?.techniques?.let { techniques ->
                if (techniques.isNotEmpty()) {
                    StudioDetailItem(
                        label = "The Technique",
                        value = techniques.joinToString(", ")
                    )
                }
            }
        }
    }
}

@Composable
private fun StudioDetailItem(label: String, value: String) {
    Column {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            color = Color(0xFF7A6A53),
            letterSpacing = 1.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            color = Color(0xFF2F2A24)
        )
    }
}

@Composable
private fun AvailableWorksSection(
    products: List<ArtisanProduct>,
    onProductClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Available Works",
            fontFamily = NotoSerif,
            fontSize = 28.sp,
            color = Color(0xFF7A6A53),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = "Current pieces currently available.",
            fontSize = 13.sp,
            color = Color(0xFF7F7468),
            modifier = Modifier.padding(bottom = 18.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            products.forEach { product ->
                ProductListItem(
                    product = product,
                    onClick = { onProductClick(product.slug) }
                )
            }
        }
    }
}

@Composable
private fun ProductListItem(
    product: ArtisanProduct,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            AsyncImage(
                model = product.realImageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3f),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontFamily = NotoSerif,
                    fontSize = 20.sp,
                    color = BrandDeepBrown
                )
                val subtitleParts = listOfNotNull(
                    product.material?.uppercase(),
                    product.collection?.uppercase()
                )
                if (subtitleParts.isNotEmpty()) {
                    Text(
                        text = subtitleParts.joinToString(" · "),
                        fontSize = 10.sp,
                        color = BrandSand,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Text(
                text = "${product.currency}${formatPrice(product.price)}",
                color = BrandGold,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun NewsletterSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFE8DF))
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "\u2709",
                fontSize = 28.sp,
                color = Color(0xFF7A6A53),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Studio Letters",
                fontFamily = NotoSerif,
                fontSize = 30.sp,
                color = Color(0xFF2F2A24),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "Receive occasional updates on new firings, studio notes, and upcoming workshop dates.",
                fontSize = 14.sp,
                lineHeight = 25.sp,
                color = Color(0xFF7F7468),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .padding(bottom = 18.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.75f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Your email address",
                        fontSize = 13.sp,
                        color = Color(0xFFA29689),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .border(
                                BorderStroke(1.dp, Color(0xFF7A6A53).copy(alpha = 0.12f)),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(bottom = 10.dp)
                    )
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A6A53))
                    ) {
                        Text(
                            text = "Subscribe",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFFAF4)
                        )
                    }
                }
            }
        }
    }
}

private fun formatPrice(price: Double): String {
    return if (price == price.toLong().toDouble()) {
        price.toLong().toString()
    } else {
        String.format("%.2f", price)
    }
}
