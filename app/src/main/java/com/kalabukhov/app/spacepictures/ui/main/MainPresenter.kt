package com.kalabukhov.app.spacepictures.ui.main

import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.IScreens
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceEntity
import com.kalabukhov.app.spacepictures.rest.SpaceImageApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainPresenter(
    private val router: Router,
    private val screens: IScreens
) : MainContract.Presenter() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var imageSpaceEntity: ImageSpaceEntity

    override fun onProfileActivity() {
        router.navigateTo(screens.users())
    }

    override fun onLoadImageWeb(spaceImageApi: SpaceImageApi, dateNow: String) {
        viewState.setState(MainContract.ViewState.LOADING)
        compositeDisposable.add(spaceImageApi.getPictureOfTheDay(api_key, dateNow)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { repos ->
                    viewState.showImageWed(repos)
                    viewState.setState(MainContract.ViewState.SUCCESSFUL)
                    imageSpaceEntity = repos
                } ,
                { thr ->
                    viewState.errorLoadImage(thr.toString())
                    viewState.setState(MainContract.ViewState.ERROR)
                }))
    }

    override fun onSaveImageDb(imageRepo: ImageSpaceRepo) {
        val imageSpaceDbEntity = ImageSpaceDbEntity(
            UUID.randomUUID().toString(),
            imageSpaceEntity.title.toString(),
            imageSpaceEntity.url.toString(),
            imageSpaceEntity.explanation.toString()
        )
        imageRepo.put(imageSpaceDbEntity)
        viewState.setState(MainContract.ViewState.DOWNLOAD)
    }

    companion object {
        const val api_key = "api_key"
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}