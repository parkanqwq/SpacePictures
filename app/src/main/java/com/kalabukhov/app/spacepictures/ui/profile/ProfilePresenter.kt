package com.kalabukhov.app.spacepictures.ui.profile

import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import io.reactivex.disposables.CompositeDisposable

class ProfilePresenter : ProfileContract.Presenter() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onLoadingImageDb(imageSpaceRepo: ImageSpaceRepo) {
        viewState.setState(ProfileContract.ViewState.LOADING)
        compositeDisposable.add(
            imageSpaceRepo.image.subscribe{
                viewState.getImageForAdapter(it)
                viewState.setState(ProfileContract.ViewState.SUCCESSFUL)
            }
        )
    }

    override fun onDeleteAll(imageSpaceRepo: ImageSpaceRepo) {
        imageSpaceRepo.clear()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
