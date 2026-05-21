package com.otaku.terraformstudio.features.checkout.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.otaku.terraformstudio.ui.theme.BrandGold
import com.otaku.terraformstudio.ui.theme.BrandMutedGold

@Composable
fun StepProgressBar(
    currentStep: CheckoutStep,
    modifier: Modifier = Modifier,
) {
    val stepNumber = when (currentStep) {
        CheckoutStep.SHIPPING -> 1
        CheckoutStep.PAYMENT -> 2
        CheckoutStep.REVIEW -> 3
    }
    val progress = stepNumber / 3f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Step $stepNumber of 3",
            fontSize = 11.sp,
            letterSpacing = 0.25.sp,
            color = BrandMutedGold,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(end = 12.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(BrandMutedGold.copy(alpha = 0.2f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(BrandGold),
            )
        }
    }
}
