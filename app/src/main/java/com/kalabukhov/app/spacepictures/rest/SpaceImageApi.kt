package com.kalabukhov.app.spacepictures.rest

import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SpaceImageApi {
    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Single<ImageSpaceEntity>
}