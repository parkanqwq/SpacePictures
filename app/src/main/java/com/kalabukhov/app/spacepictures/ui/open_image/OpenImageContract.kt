package com.kalabukhov.app.spacepictures.ui.open_image

import com.kalabukhov.app.spacepictures.App
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

class OpenImageContract {
    enum class ViewState {
        DELETE
    }

    interface View: MvpView {
        @AddToEnd
        fun setState(state: ViewState)
    }

    abstract class Presenter: MvpPresenter<View>() {
        abstract fun onDelete(app: App, imageSpaceDbEntity: ImageSpaceDbEntity)
    }
}