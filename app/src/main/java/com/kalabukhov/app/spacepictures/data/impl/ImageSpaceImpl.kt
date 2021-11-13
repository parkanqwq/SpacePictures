package com.kalabukhov.app.spacepictures.data.impl

import com.kalabukhov.app.spacepictures.data.ImageSpaceDao
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageSpaceImpl(private val imageSpaceDao: ImageSpaceDao): ImageSpaceRepo {
    override val image: Observable<List<ImageSpaceDbEntity>> =
        imageSpaceDao.getImagesDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun put(image: ImageSpaceDbEntity) {
        imageSpaceDao.put(image)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun clear() {
        imageSpaceDao.clear()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun delete(image: ImageSpaceDbEntity) {
        imageSpaceDao.delete(image)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}