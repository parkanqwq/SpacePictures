package com.kalabukhov.app.spacepictures.domain

import com.squareup.moshi.Json

data class ImageSpaceEntity(
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "date") val date: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "explanation") val explanation: String
)

