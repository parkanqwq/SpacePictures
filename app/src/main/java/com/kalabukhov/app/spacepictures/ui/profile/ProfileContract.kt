package com.kalabukhov.app.spacepictures.ui.profile

import com.kalabukhov.app.spacepictures.App
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

class ProfileContract {

    enum class ViewState {
        LOADING, SUCCESSFUL, ERROR
    }

    interface View: MvpView {
        @AddToEnd
        fun setState(state: ViewState)
        @AddToEnd
        fun getImageForAdapter(imageSpaceDbEntity: List<ImageSpaceDbEntity>)
    }

    abstract class Presenter: MvpPresenter<View>() {
        abstract fun onLoadingImageDb(app: App)
        abstract fun onDeleteAll(app: App)
    }
}