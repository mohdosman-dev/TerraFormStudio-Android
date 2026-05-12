package com.otaku.terraformstudio.features.makers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.otaku.terraformstudio.core.presentation.ObserveAsEvents
import com.otaku.terraformstudio.features.makers.domain.MakersArtisan
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandDeepBrown
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandMutedGold
import com.otaku.terraformstudio.ui.theme.BrandSand
import org.koin.androidx.compose.koinViewModel

@Composable
fun MakersRoot(
    onNavigateToArtisan: (String) -> Unit,
    viewModel: MakersViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MakersEvent.NavigateToArtisan -> onNavigateToArtisan(event.slug)
        }
    }

    MakersScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MakersScreen(
    state: MakersState,
    onAction: (MakersAction) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= state.artisans.size - 3 && !state.isLoadingMore && state.page < state.totalPages
        }
    }

    if (shouldLoadMore) {
        onAction(MakersAction.OnLoadMore)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(BrandCream)
    ) {
        item {
            DirectoryHero()
        }

        itemsIndexed(state.artisans, key = { _, a -> a.id }) { index, artisan ->
            ArtisanArticle(
                artisan = artisan,
                index = index,
                onClick = { onAction(MakersAction.OnArtisanClick(artisan.slug)) }
            )
        }

        if (state.isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = BrandGold,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        }

        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrandGold)
                }
            }
        }

        if (state.error != null && state.artisans.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error.asString(),
                        color = BrandMutedGold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DirectoryHero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column {
            Text(
                text = "The Hands Behind",
                fontFamily = NotoSerif,
                fontSize = 48.sp,
                lineHeight = 49.sp,
                fontWeight = FontWeight.Normal,
                color = BrandGold
            )
            Text(
                text = "The Vessel",
                fontFamily = NotoSerif,
                fontSize = 48.sp,
                lineHeight = 49.sp,
                fontWeight = FontWeight.Normal,
                color = BrandGold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "A curated directory of the world's most dedicated ceramicists, each preserving the ancient dialogue between earth and flame.",
                fontSize = 16.sp,
                lineHeight = 29.sp,
                color = BrandMutedGold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(8.dp))
                .background(BrandSand.copy(alpha = 0.3f))
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBWAQfiRi1cTTZGHGZC5lAiDwvm5ETFEyXcGnbqpAoB3D4NS7nI_kZfX4724yWZzNHJTVuegwwTQvtOPDum1mGI0W5pAr2gTPys8vbmBym7N-seg6oTmHH5aLh3Bo3KA67kmwI3GJ_DwsnhavV0JFn9w1KFZBsgHksbTHRVcb3i2U-UTtL2nXsvlCqZiOyKIlomPuWdN-rTFQ91xeINTTtLCYGp_tJTWoXZqYM3sncdhO6nCjB2zbFzHFtZGaYKtKaOVPldbsq73Ck",
                contentDescription = "Master ceramicist shaping clay on a pottery wheel",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ArtisanArticle(
    artisan: MakersArtisan,
    index: Int,
    onClick: () -> Unit
) {
    val pattern = index % 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 84.dp)
    ) {
        when (pattern) {
            0 -> TextThenImageArticle(artisan = artisan, onClick = onClick, imageAspect = 3f / 4f)
            1 -> ImageThenTextArticle(artisan = artisan, onClick = onClick)
            2 -> TextThenImageArticle(artisan = artisan, onClick = onClick, imageAspect = 4f / 5f)
        }
    }
}

@Composable
private fun TextThenImageArticle(
    artisan: MakersArtisan,
    onClick: () -> Unit,
    imageAspect: Float
) {
    ArticleTextContent(artisan = artisan, onClick = onClick)

    Spacer(modifier = Modifier.height(28.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageAspect)
            .clip(RoundedCornerShape(16.dp))
            .background(BrandSand.copy(alpha = 0.2f))
    ) {
        AsyncImage(
            model = artisan.realHeroImageUrl,
            contentDescription = artisan.heroImageAlt,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ImageThenTextArticle(
    artisan: MakersArtisan,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clip(RoundedCornerShape(16.dp))
            .background(BrandSand.copy(alpha = 0.2f))
    ) {
        AsyncImage(
            model = artisan.realHeroImageUrl,
            contentDescription = artisan.heroImageAlt,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    Spacer(modifier = Modifier.height(28.dp))

    ArticleTextContent(artisan = artisan, onClick = onClick)
}

@Composable
private fun ArticleTextContent(
    artisan: MakersArtisan,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = artisan.location?.uppercase() ?: "",
            fontSize = 10.sp,
            letterSpacing = 1.6.sp,
            color = BrandMutedGold,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = artisan.displayName,
            fontFamily = NotoSerif,
            fontSize = 42.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Normal,
            color = BrandGold
        )

        Spacer(modifier = Modifier.height(18.dp))

        artisan.bioShort?.let { quote ->
            Text(
                text = "\u201C$quote\u201D",
                fontFamily = NotoSerif,
                fontSize = 22.sp,
                lineHeight = 34.sp,
                fontStyle = FontStyle.Italic,
                color = BrandDeepBrown
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Text(
                text = "Explore Works",
                fontSize = 12.sp,
                letterSpacing = 1.68.sp,
                fontWeight = FontWeight.Bold,
                color = BrandGold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "\u2192",
                fontSize = 18.sp,
                color = BrandGold
            )
        }
    }
}

private val NotoSerif = androidx.compose.ui.text.font.FontFamily.Serif
