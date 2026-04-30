package com.otaku.terraformstudio.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomNavigation(
    currentRoute: String? = null,
    onNavigate: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "gallery",
            onClick = { onNavigate("gallery") },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Gallery") },
            label = { Text("Gallery") }
        )
        NavigationBarItem(
            selected = currentRoute == "artisans",
            onClick = { onNavigate("artisans") },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Artisans") },
            label = { Text("Artisans") }
        )
        NavigationBarItem(
            selected = currentRoute == "journal",
            onClick = { onNavigate("journal") },
            icon = { Icon(Icons.Default.Menu, contentDescription = "Journal") },
            label = { Text("Journal") }
        )
        NavigationBarItem(
            selected = currentRoute == "cart",
            onClick = { onNavigate("cart") },
            icon = { Icon(Icons.Default.ShoppingBag, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
    }
}
