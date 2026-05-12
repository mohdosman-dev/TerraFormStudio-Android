package com.otaku.terraformstudio.features.auth.domain

data class User(
    val id: String,
    val email: String,
    val roles: List<String>
)
