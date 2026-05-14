package com.otaku.terraformstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.otaku.terraformstudio.features.artisan.presentation.ArtisanProfileRoot
import com.otaku.terraformstudio.features.home.presentation.HomeRoot
import com.otaku.terraformstudio.features.product.presentation.ProductDetailRoot
import com.otaku.terraformstudio.ui.theme.TerraFormStudioTheme
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class ProductDetailRoute(val slug: String)

@Serializable
data class ArtisanProfileRoute(val slug: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TerraFormStudioTheme {
                TerraFormNavigation()
            }
        }
    }
}

@Composable
fun TerraFormNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeRoot(
                onNavigateToProduct = { slug ->
                    navController.navigate(ProductDetailRoute(slug))
                },
                onNavigateToArtisan = { slug ->
                    navController.navigate(ArtisanProfileRoute(slug))
                },
                onNavigateToCollection = { collectionId ->
                    // navController.navigate(CollectionRoute(collectionId))
                }
            )
        }
        composable<ProductDetailRoute> { backStackEntry ->
            val route: ProductDetailRoute = backStackEntry.toRoute()
            ProductDetailRoot(
                slug = route.slug,
                onBackClick = { navController.popBackStack() },
                onNavigateToProduct = { slug ->
                    navController.navigate(ProductDetailRoute(slug))
                },
                onNavigateToArtisan = { slug ->
                    navController.navigate(ArtisanProfileRoute(slug))
                }
            )
        }
        composable<ArtisanProfileRoute> { backStackEntry ->
            val route: ArtisanProfileRoute = backStackEntry.toRoute()
            ArtisanProfileRoot(
                slug = route.slug,
                onBackClick = { navController.popBackStack() },
                onNavigateToProduct = { slug ->
                    navController.navigate(ProductDetailRoute(slug))
                }
            )
        }
    }
}
