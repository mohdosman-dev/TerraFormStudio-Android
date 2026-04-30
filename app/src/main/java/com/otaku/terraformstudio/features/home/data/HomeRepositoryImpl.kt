package com.otaku.terraformstudio.features.home.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.home.domain.HomeConfiguration
import com.otaku.terraformstudio.features.home.domain.HomeRepository
import retrofit2.HttpException
import java.io.IOException

class HomeRepositoryImpl(
    private val homeApi: HomeApi
) : HomeRepository {
    override suspend fun getHomeConfiguration(): Result<HomeConfiguration, DataError.Network> {
        return try {
            val dto = homeApi.getHomeConfiguration()
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                404 -> Result.Error(DataError.Network.NOT_FOUND)
                else -> Result.Error(DataError.Network.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}
