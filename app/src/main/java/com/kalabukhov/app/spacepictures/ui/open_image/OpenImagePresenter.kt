package com.kalabukhov.app.spacepictures.ui.open_image

import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.App
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity

class OpenImagePresenter(
    private val router: Router
) : OpenImageContract.Presenter() {

    override fun onDelete(app: App, imageSpaceDbEntity: ImageSpaceDbEntity) {
        app.imageRepo.delete(imageSpaceDbEntity)
        viewState.setState(OpenImageContract.ViewState.DELETE)
        router.exit()
    }
}