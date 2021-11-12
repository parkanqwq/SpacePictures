package com.kalabukhov.app.spacepictures.ui.open_image

import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity

class OpenImagePresenter(
    private val router: Router
) : OpenImageContract.Presenter() {

    override fun onDelete(imageRepo: ImageSpaceRepo, imageSpaceDbEntity: ImageSpaceDbEntity) {
        imageRepo.delete(imageSpaceDbEntity)
        viewState.setState(OpenImageContract.ViewState.DELETE)
        router.exit()
    }
}