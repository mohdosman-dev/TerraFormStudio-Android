package com.otaku.terraformstudio.features.auth.domain

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result

interface AuthRepository {
    suspend fun signup(email: String, password: String): Result<User, DataError.Network>
    suspend fun login(email: String, password: String): Result<User, DataError.Network>
    suspend fun getCurrentUser(): Result<User, DataError.Network>
    fun logout()
    fun isAuthenticated(): Boolean
}
