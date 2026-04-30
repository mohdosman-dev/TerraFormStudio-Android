package com.otaku.terraformstudio.features.home.data

import com.otaku.terraformstudio.core.domain.DataError
import com.otaku.terraformstudio.core.domain.Result
import com.otaku.terraformstudio.features.home.domain.HomeArtisan
import com.otaku.terraformstudio.features.home.domain.HomeCollection
import com.otaku.terraformstudio.features.home.domain.HomeConfiguration
import com.otaku.terraformstudio.features.home.domain.HomeProduct
import com.otaku.terraformstudio.features.home.domain.HomeSection
import com.otaku.terraformstudio.features.home.domain.HomeSectionType
import com.otaku.terraformstudio.features.home.domain.HomeRepository
import org.koin.core.annotation.Single
import retrofit2.HttpException
import java.io.IOException

@Single
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

fun HomeConfigurationDto.toDomain(): HomeConfiguration {
    return HomeConfiguration(
        sections = sections.map { it.toDomain() }.sortedBy { it.sortOrder }
    )
}

fun HomeSectionDto.toDomain(): HomeSection {
    return HomeSection(
        type = when (type) {
            "hero" -> HomeSectionType.HERO
            "artisan_spotlight" -> HomeSectionType.ARTISAN_SPOTLIGHT
            "product_row" -> HomeSectionType.PRODUCT_ROW
            "collection_row" -> HomeSectionType.COLLECTION_ROW
            "editorial" -> HomeSectionType.EDITORIAL
            else -> HomeSectionType.UNKNOWN
        },
        title = title,
        subtitle = subtitle,
        imageUrl = image?.url,
        imageAlt = image?.alt,
        ctaLabel = cta?.label,
        ctaTarget = cta?.targetId ?: cta?.url,
        content = content,
        artisan = artisanId?.let { HomeArtisan(it._id, it.name, it.philosophy, it.studioImage) },
        products = productIds?.map { HomeProduct(it._id, it.title, it.price, it.primaryImage) } ?: emptyList(),
        collections = collectionIds?.map { HomeCollection(it._id, it.title, it.image) } ?: emptyList(),
        sortOrder = sortOrder
    )
}
