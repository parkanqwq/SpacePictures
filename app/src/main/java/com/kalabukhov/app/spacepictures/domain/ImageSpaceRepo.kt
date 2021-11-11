package com.kalabukhov.app.spacepictures.domain

import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import io.reactivex.Observable

interface ImageSpaceRepo {
    val image: Observable<List<ImageSpaceDbEntity>>
    fun put(image: ImageSpaceDbEntity)
    fun clear()
    fun delete(image: ImageSpaceDbEntity)
}