package com.kalabukhov.app.spacepictures.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity

@Database(
    entities = [ImageSpaceDbEntity::class],
    version = 1
)

abstract class ImageSpaceDb : RoomDatabase() {
    abstract fun imageSpaceDao(): ImageSpaceDao
}