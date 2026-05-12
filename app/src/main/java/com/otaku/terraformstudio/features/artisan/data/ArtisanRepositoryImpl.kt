package com.otaku.terraformstudio.features.artisan.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.artisan.domain.ArtisanProfile
import com.otaku.terraformstudio.features.artisan.domain.ArtisanRepository
import retrofit2.HttpException
import java.io.IOException

class ArtisanRepositoryImpl(
    private val api: ArtisanApi
) : ArtisanRepository {
    override suspend fun getArtisanProfile(slug: String): Result<ArtisanProfile, DataError.Network> {
        return try {
            val dto = api.getArtisanProfile(slug)
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
