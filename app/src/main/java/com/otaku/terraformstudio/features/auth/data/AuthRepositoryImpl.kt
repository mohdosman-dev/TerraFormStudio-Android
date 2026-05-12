package com.otaku.terraformstudio.features.auth.data

import com.otaku.terraformstudio.core.data.local.AuthTokenManager
import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.auth.domain.AuthRepository
import com.otaku.terraformstudio.features.auth.domain.User
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenManager: AuthTokenManager
) : AuthRepository {

    override suspend fun signup(email: String, password: String): Result<User, DataError.Network> {
        return handleApiCall {
            val response = authApi.signup(SignupRequest(email, password))
            // After signup, the backend doesn't return a token in your routes, only message + user
            // You might need to login after signup or modify backend to return token.
            // For now, mapping response user to domain user.
            response.user.toUser()
        }
    }

    override suspend fun login(email: String, password: String): Result<User, DataError.Network> {
        return handleApiCall {
            val response = authApi.login(LoginRequest(email, password))
            response.token?.let { tokenManager.saveToken(it) }
            response.user.toUser()
        }
    }

    override suspend fun getCurrentUser(): Result<User, DataError.Network> {
        return handleApiCall {
            authApi.me().user.toUser()
        }
    }

    override fun logout() {
        tokenManager.clearToken()
    }

    override fun isAuthenticated(): Boolean {
        return tokenManager.getToken() != null
    }

    private suspend fun <T> handleApiCall(call: suspend () -> T): Result<T, DataError.Network> {
        return try {
            Result.Success(call())
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Result.Error(DataError.Network.BAD_REQUEST)
                401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                403 -> Result.Error(DataError.Network.FORBIDDEN)
                404 -> Result.Error(DataError.Network.NOT_FOUND)
                408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                409 -> Result.Error(DataError.Network.CONFLICT)
                413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
                429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: Exception) {
            Result.Error(DataError.Network.SERIALIZATION)
        }
    }
}
