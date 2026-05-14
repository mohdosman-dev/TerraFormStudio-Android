package com.otaku.terraformstudio.features.makers.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.makers.domain.MakersList
import com.otaku.terraformstudio.features.makers.domain.MakersRepository
import retrofit2.HttpException
import java.io.IOException

class MakersRepositoryImpl(
    private val api: MakersApi
) : MakersRepository {
    override suspend fun getArtisans(page: Int, limit: Int): Result<MakersList, DataError.Network> {
        return try {
            val dto = api.getArtisans(page = page, limit = limit)
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
