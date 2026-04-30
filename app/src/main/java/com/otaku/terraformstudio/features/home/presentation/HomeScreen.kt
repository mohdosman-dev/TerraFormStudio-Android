package com.otaku.terraformstudio.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.otaku.terraformstudio.features.home.domain.HomeArtisan
import com.otaku.terraformstudio.features.home.domain.HomeCollection
import com.otaku.terraformstudio.features.home.domain.HomeProduct
import com.otaku.terraformstudio.features.home.domain.HomeSection
import com.otaku.terraformstudio.features.home.domain.HomeSectionType
import com.otaku.terraformstudio.ui.theme.NotoSerif
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
            is HomeEvent.NavigateToProduct -> onNavigateToProduct(event.productId)
            is HomeEvent.NavigateToArtisan -> onNavigateToArtisan(event.artisanId)
            is HomeEvent.NavigateToCollection -> onNavigateToCollection(event.collectionId)
            is HomeEvent.ShowError -> { /* Show Snackbar or similar */ }
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terra Form Studio",
                        fontFamily = NotoSerif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 22.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Cart */ }) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.88f)
                )
            )
        },
        bottomBar = {
            HomeBottomNavigation()
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
        HomeSectionType.EDITORIAL -> HomeEditorialSection(section, onAction)
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
                HomeCollectionCard(collection, onAction)
            }
        }
    }
}

@Composable
fun HomeCollectionCard(collection: HomeCollection, onAction: (HomeAction) -> Unit) {
    Column(
        modifier = Modifier
            .width(146.dp)
            .clickable { onAction(HomeAction.OnCollectionClick(collection.id)) }
    ) {
        AsyncImage(
            model = collection.imageUrl,
            contentDescription = collection.title,
            modifier = Modifier
                .height(194.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = collection.title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomeArtisanSpotlight(section: HomeSection, onAction: (HomeAction) -> Unit) {
    val artisan = section.artisan ?: return
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 28.dp)
            .fillMaxWidth()
            .clickable { onAction(HomeAction.OnArtisanClick(artisan.id)) },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = artisan.studioImageUrl,
                contentDescription = artisan.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Artisan Spotlight".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.title ?: "The Soul of Clay: ${artisan.name}",
                style = MaterialTheme.typography.headlineLarge,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = section.content ?: artisan.philosophy ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Read her story".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
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
        // Simplified grid for now using chunks
        val chunks = section.products.chunked(2)
        chunks.forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowProducts.forEach { product ->
                    HomeProductCard(
                        product = product,
                        onAction = onAction,
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

@Composable
fun HomeProductCard(product: HomeProduct, onAction: (HomeAction) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable { onAction(HomeAction.OnProductClick(product.id)) }
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.title,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = product.title, style = MaterialTheme.typography.titleLarge, fontSize = 15.sp)
        Text(
            text = "$${String.format("%.2f", product.price)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HomeEditorialSection(section: HomeSection, onAction: (HomeAction) -> Unit) {
    // Implement based on design if needed
}

@Composable
fun HomeBottomNavigation() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Menu, contentDescription = "Gallery") },
            label = { Text("Gallery") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Menu, contentDescription = "Artisans") },
            label = { Text("Artisans") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Menu, contentDescription = "Journal") },
            label = { Text("Journal") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
    }
}
