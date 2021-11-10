package com.kalabukhov.app.spacepictures.ui.main

import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.App
import com.kalabukhov.app.spacepictures.Screens
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(
    private val router: Router
    ) : MainContract.Presenter() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onProfileActivity() {
        router.navigateTo(Screens.Main())
    }

    override fun onLoadImageWeb(app: App, dateNow: String) {
        viewState.setState(MainContract.ViewState.LOADING)
        compositeDisposable.add(app.spaceImageApi.getPictureOfTheDay(api_key, dateNow)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { repos ->
                    viewState.showImageWed(repos)
                    viewState.setState(MainContract.ViewState.SUCCESSFUL)
                } ,
                { thr ->
                    viewState.errorLoadImage(thr.toString())
                    viewState.setState(MainContract.ViewState.ERROR)
                }))
    }

    companion object {
        const val api_key = "api_key"
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}