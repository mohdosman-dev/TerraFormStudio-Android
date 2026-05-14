package com.otaku.terraformstudio.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otaku.terraformstudio.R
import com.otaku.terraformstudio.ui.theme.BrandCream
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandMutedGold

@Composable
fun AppBottomNavigation(
    currentRoute: String? = null,
    onNavigate: (String) -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, BrandGold.copy(alpha = 0.08f)),
        color = BrandCream.copy(alpha = 0.96f)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .navigationBarsPadding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavItem(
                label = "Discover",
                iconRes = R.drawable.ic_nav_gallery,
                isSelected = currentRoute == "discover",
                onClick = { onNavigate("discover") }
            )
            NavItem(
                label = "Artists",
                iconRes = R.drawable.ic_nav_artisans,
                isSelected = currentRoute == "artists",
                onClick = { onNavigate("artists") }
            )
            NavItem(
                label = "Collections",
                iconRes = R.drawable.ic_nav_journal,
                isSelected = currentRoute == "collections",
                onClick = { onNavigate("collections") }
            )
            NavItem(
                label = "Cart",
                iconRes = R.drawable.ic_nav_cart,
                isSelected = currentRoute == "cart",
                onClick = { onNavigate("cart") }
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) BrandGold else BrandMutedGold
    
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(18.dp),
                tint = contentColor.copy(alpha = if (isSelected) 1f else 0.4f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                color = contentColor
            )
        }
        // Click surface
        Surface(
            color = Color.Transparent,
            onClick = onClick,
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}
