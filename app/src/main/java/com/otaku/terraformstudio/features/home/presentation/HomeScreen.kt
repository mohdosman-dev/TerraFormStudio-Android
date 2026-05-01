package com.otaku.terraformstudio.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.core.presentation.components.*
import com.otaku.terraformstudio.features.home.domain.HomeSection
import com.otaku.terraformstudio.features.home.domain.HomeSectionType
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoot(
    onNavigateToProduct: (String) -> Unit,
    onNavigateToArtisan: (String) -> Unit,
    onNavigateToCollection: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.NavigateToProduct -> onNavigateToProduct(event.slug)
            is HomeEvent.NavigateToArtisan -> onNavigateToArtisan(event.artisanId)
            is HomeEvent.NavigateToCollection -> onNavigateToCollection(event.collectionId)
            is HomeEvent.ShowError -> { /* Show Snackbar */ }
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                onMenuClick = { /* Open Drawer */ },
                onCartClick = { /* Open Cart */ }
            )
        },
        bottomBar = {
            AppBottomNavigation(currentRoute = "gallery")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            state.homeConfiguration?.sections?.let { sections ->
                items(sections) { section ->
                    HomeSectionItem(section = section, onAction = onAction)
                }
            }

            if (state.isLoading) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeSectionItem(
    section: HomeSection,
    onAction: (HomeAction) -> Unit
) {
    when (section.type) {
        HomeSectionType.HERO -> HomeHeroSection(section, onAction)
        HomeSectionType.COLLECTION_ROW -> HomeCollectionRow(section, onAction)
        HomeSectionType.ARTISAN_SPOTLIGHT -> HomeArtisanSpotlight(section, onAction)
        HomeSectionType.PRODUCT_ROW -> HomeProductRow(section, onAction)
        else -> {}
    }
}

@Composable
fun HomeHeroSection(section: HomeSection, onAction: (HomeAction) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
    ) {
        AsyncImage(
            model = section.imageUrl,
            contentDescription = section.imageAlt,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.85f)
                        ),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            section.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displayLarge,
                    lineHeight = 42.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = { section.ctaTarget?.let { onAction(HomeAction.OnCtaClick(section.ctaLabel ?: "", it)) } },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = section.ctaLabel ?: "Shop the Collection")
            }
        }
    }
}

@Composable
fun HomeCollectionRow(section: HomeSection, onAction: (HomeAction) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 28.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = section.title ?: "Collections", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "View All",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { /* View All */ }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(section.collections) { collection ->
                CollectionCard(
                    id = collection.id,
                    title = collection.title,
                    imageUrl = collection.imageUrl,
                    onClick = { onAction(HomeAction.OnCollectionClick(it)) }
                )
            }
        }
    }
}

@Composable
fun HomeArtisanSpotlight(section: HomeSection, onAction: (HomeAction) -> Unit) {
    val artisan = section.artisan ?: return
    ArtisanSpotlightCard(
        id = artisan.id,
        name = artisan.name,
        title = section.title ?: "The Soul of Clay: ${artisan.name}",
        content = section.content ?: artisan.philosophy ?: "",
        studioImageUrl = artisan.studioImageUrl,
        onClick = { onAction(HomeAction.OnArtisanClick(it)) },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 28.dp)
    )
}

@Composable
fun HomeProductRow(section: HomeSection, onAction: (HomeAction) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 28.dp)) {
        section.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )
        }
        val chunks = section.products.chunked(2)
        chunks.forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowProducts.forEach { product ->
                    ProductCard(
                        id = product.id,
                        slug = product.slug,
                        title = product.title,
                        price = product.price,
                        imageUrl = product.imageUrl,
                        onClick = { onAction(HomeAction.OnProductClick(it)) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowProducts.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
