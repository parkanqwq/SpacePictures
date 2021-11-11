package com.kalabukhov.app.spacepictures.ui.profile

import android.widget.Toast
import com.kalabukhov.app.spacepictures.App
import io.reactivex.disposables.CompositeDisposable

class ProfilePresenter : ProfileContract.Presenter() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onLoadingImageDb(app: App) {
        viewState.setState(ProfileContract.ViewState.LOADING)
        compositeDisposable.add(
            app.imageRepo.image.subscribe{
                viewState.getImageForAdapter(it)
                viewState.setState(ProfileContract.ViewState.SUCCESSFUL)
            }
        )
    }

    override fun onDeleteAll(app: App) {
        app.imageRepo.clear()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
