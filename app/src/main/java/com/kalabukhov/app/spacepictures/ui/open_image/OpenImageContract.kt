package com.kalabukhov.app.spacepictures.ui.open_image

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

class OpenImageContract {
    enum class ViewState {
        DELETE, SAVE, NOT_SAVE
    }

    interface View: MvpView {
        @AddToEnd
        fun setState(state: ViewState)
    }

    abstract class Presenter: MvpPresenter<View>() {
        abstract fun onDelete(imageRepo: ImageSpaceRepo, imageSpaceDbEntity: ImageSpaceDbEntity)
        abstract fun onSaveImageToPhone(bitmap: Bitmap, contentResolver: ContentResolver, intent: Intent)
    }
}