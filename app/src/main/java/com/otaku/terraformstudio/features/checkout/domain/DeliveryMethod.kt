package com.otaku.terraformstudio.features.checkout.domain

data class DeliveryMethod(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val currency: String,
    val estimatedDays: String,
    val isActive: Boolean,
    val isDefault: Boolean,
)
