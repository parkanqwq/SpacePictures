package com.kalabukhov.app.spacepictures.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ImageSpaceDao {
        @Query("SELECT * FROM images")
        fun getImagesDb() : Observable<List<ImageSpaceDbEntity>>

        @Query("DELETE FROM images")
        fun clear() : Completable

        @Insert
        fun put(user: ImageSpaceDbEntity) : Completable

        @Delete
        fun delete(user: ImageSpaceDbEntity) : Completable
}