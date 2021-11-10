package com.kalabukhov.app.spacepictures.ui.main

import com.kalabukhov.app.spacepictures.App
import com.kalabukhov.app.spacepictures.domain.ImageSpaceEntity
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEnd

class MainContract {

    enum class ViewState {
        LOADING, SUCCESSFUL, ERROR
    }

    interface View: MvpView {
        @AddToEnd
        fun setState(state: ViewState)
        @AddToEnd
        fun loadImageWeb(dateNow: String)
        @AddToEnd
        fun showImageWed(imageSpaceEntity: ImageSpaceEntity)
        @AddToEnd
        fun errorLoadImage(thr: String)
    }

    abstract class Presenter: MvpPresenter<View>() {
        abstract fun onProfileActivity()
        abstract fun onLoadImageWeb(app: App, dateNow: String)
    }
}