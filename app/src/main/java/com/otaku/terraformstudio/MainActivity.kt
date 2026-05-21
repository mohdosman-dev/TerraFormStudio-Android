package com.otaku.terraformstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.otaku.terraformstudio.core.data.local.AuthTokenManager
import com.otaku.terraformstudio.features.artisan.presentation.ArtisanProfileRoot
import com.otaku.terraformstudio.features.auth.presentation.signin.SignInRoot
import com.otaku.terraformstudio.features.auth.presentation.signup.SignUpRoot
import com.otaku.terraformstudio.features.cart.presentation.CartRoot
import com.otaku.terraformstudio.features.checkout.presentation.CheckoutRoot
import com.otaku.terraformstudio.features.checkout.presentation.OrderConfirmationRoot
import com.otaku.terraformstudio.features.home.presentation.HomeRoot
import com.otaku.terraformstudio.features.product.presentation.ProductDetailRoot
import com.otaku.terraformstudio.ui.theme.TerraFormStudioTheme
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.get

@Serializable
object HomeRoute

@Serializable
data class ProductDetailRoute(val slug: String)

@Serializable
data class ArtisanProfileRoute(val slug: String)

@Serializable
object CartRoute

@Serializable
data class CheckoutRoute(val cartId: String)

@Serializable
data class OrderConfirmationRoute(val orderId: String, val grandTotal: Double)

@Serializable
object SignInRoute

@Serializable
object SignUpRoute

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
    val authTokenManager: AuthTokenManager = get(AuthTokenManager::class.java)
    var pendingCheckoutCartId by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeRoot(
                onNavigateToCart = {
                    navController.navigate(CartRoute)
                },
                onNavigateToProduct = { slug ->
                    navController.navigate(ProductDetailRoute(slug))
                },
                onNavigateToArtisan = { slug ->
                    navController.navigate(ArtisanProfileRoute(slug))
                },
                onNavigateToCollection = { }
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
        composable<CartRoute> {
            CartRoot(
                onBackClick = { navController.popBackStack() },
                onNavigateToCheckout = { cartId ->
                    if (authTokenManager.getToken() != null) {
                        navController.navigate(CheckoutRoute(cartId)) {
                            launchSingleTop = true
                        }
                    } else {
                        pendingCheckoutCartId = cartId
                        navController.navigate(SignInRoute)
                    }
                },
                onNavigateToDiscover = {
                    navController.popBackStack()
                },
                onNavigateToProduct = { slug ->
                    navController.navigate(ProductDetailRoute(slug))
                }
            )
        }
        composable<CheckoutRoute> { backStackEntry ->
            val route: CheckoutRoute = backStackEntry.toRoute()
            CheckoutRoot(
                cartId = route.cartId,
                onBackClick = { navController.popBackStack() },
                onOrderPlaced = { orderId, grandTotal ->
                    navController.navigate(OrderConfirmationRoute(orderId, grandTotal)) {
                        popUpTo(HomeRoute)
                    }
                },
            )
        }
        composable<OrderConfirmationRoute> { backStackEntry ->
            val route: OrderConfirmationRoute = backStackEntry.toRoute()
            OrderConfirmationRoot(
                orderId = route.orderId,
                grandTotal = route.grandTotal,
                onContinueShopping = {
                    navController.navigate(HomeRoute) {
                        popUpTo(HomeRoute) { inclusive = true }
                    }
                },
            )
        }
        composable<SignInRoute> {
            SignInRoot(
                onSignInSuccess = {
                    navController.popBackStack()
                    pendingCheckoutCartId?.let { cartId ->
                        pendingCheckoutCartId = null
                        navController.navigate(CheckoutRoute(cartId))
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(SignUpRoute)
                },
            )
        }
        composable<SignUpRoute> {
            SignUpRoot(
                onSignUpSuccess = {
                    navController.popBackStack()
                    pendingCheckoutCartId?.let { cartId ->
                        pendingCheckoutCartId = null
                        navController.navigate(CheckoutRoute(cartId))
                    }
                },
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
            )
        }
    }
}
