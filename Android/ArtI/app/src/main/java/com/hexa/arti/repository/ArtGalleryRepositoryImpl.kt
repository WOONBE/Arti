package com.hexa.arti.repository

import com.google.gson.Gson
import com.hexa.arti.data.model.artmuseum.ArtGalleryResponse
import com.hexa.arti.data.model.artmuseum.CreateThemeDto
import com.hexa.arti.data.model.artmuseum.GalleryBanner
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.data.model.artmuseum.ThemeResponseItem
import com.hexa.arti.data.model.artmuseum.UpdateGalleryDto
import com.hexa.arti.data.model.artwork.Artwork
import com.hexa.arti.data.model.response.ApiException
import com.hexa.arti.data.model.response.ErrorResponse
import com.hexa.arti.network.GalleryApi
import com.hexa.arti.util.asArtwork
import com.hexa.arti.util.asGalleryBanner
import okhttp3.ResponseBody
import javax.inject.Inject

class ArtGalleryRepositoryImpl @Inject constructor(
    private val galleryAPI: GalleryApi
) : ArtGalleryRepository {

    override suspend fun getRandomGalleries(): Result<List<GalleryBanner>> {
        val result = galleryAPI.getRandomGalleries()

        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it.map { gallery -> gallery.asGalleryBanner() })
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun getRandomGenreArtworks(genreLabel: String): Result<List<Artwork>> {
        val result = galleryAPI.getRandomGenreArtworks(genreLabel)

        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it.map { getRandomGenreArtWorkResponse -> getRandomGenreArtWorkResponse.asArtwork() })
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun getArtGallery(galleryId: Int): Result<ArtGalleryResponse> {
        val result = galleryAPI.getGalley(galleryId)

        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun getArtGalleryThemes(galleryId: Int): Result<List<MyGalleryThemeItem>> {
        val result = galleryAPI.getGalleryTheme(galleryId)

        if (result.isSuccessful) {
            result.body()?.let {
                val myGalleryThemeItems = it.map { themeItem ->
                    // 각 테마 ID에 대해 테마 이미지 가져오기
                    val artworkResult = galleryAPI.getGalleryThemeArtwork(themeItem.id)
                    if (artworkResult.isSuccessful) {
                        val artworkList = artworkResult.body() ?: emptyList()
                        val imageUrls = artworkList.map { it } // 이미지 URL만 추출

                        // MyGalleryThemeItem 생성
                        MyGalleryThemeItem(
                            id = themeItem.id,
                            title = themeItem.name,
                            images = imageUrls
                        )
                    } else {
                        // 실패 시 빈 이미지 리스트로 처리
                        MyGalleryThemeItem(
                            id = themeItem.id,
                            title = themeItem.name,
                            images = emptyList()
                        )
                    }
                }
                return Result.success(myGalleryThemeItems)
            }
            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }


    override suspend fun getPostTheme(
        galleryId: Int,
        themeDto: CreateThemeDto
    ): Result<ThemeResponseItem> {
        val result = galleryAPI.postGalleryTheme(galleryId, themeDto)

        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun updateArtGallery(
        galleryId: Int,
        updateGalleryDto: UpdateGalleryDto
    ): Result<ResponseBody> {
        val result = galleryAPI.updateMyGallery(galleryId, updateGalleryDto)

        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun deleteTheme(galleryId: Int, themeId: Int): Result<ResponseBody> {
        val result = galleryAPI.deleteTheme(galleryId, themeId)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

    override suspend fun deleteThemeArtWork(
        themeId: Int,
        artworkId: Int
    ): Result<ResponseBody> {
        val result = galleryAPI.deleteThemeArtwork(themeId, artworkId)
        if (result.isSuccessful) {
            result.body()?.let {
                return Result.success(it)
            }

            return Result.failure(Exception())
        } else {
            val errorResponse =
                Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
            return Result.failure(
                ApiException(
                    code = errorResponse.code,
                    message = errorResponse.message
                )
            )
        }
    }

}