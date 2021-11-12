package com.kalabukhov.app.spacepictures.di

import android.content.Context
import androidx.room.Room
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.AndroidScreens
import com.kalabukhov.app.spacepictures.IScreens
import com.kalabukhov.app.spacepictures.data.ImageSpaceDao
import com.kalabukhov.app.spacepictures.data.ImageSpaceDb
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.impl.ImageSpaceImpl
import com.kalabukhov.app.spacepictures.rest.SpaceImageApi
import com.kalabukhov.app.spacepictures.ui.main.MainActivity
import com.kalabukhov.app.spacepictures.ui.open_image.OpenImage
import com.kalabukhov.app.spacepictures.ui.profile.Profile
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ModuleDbImage(private val context: Context) {

    @Provides
    @Singleton
    fun provideContextRepos(): Context = context

    @Provides
    @Singleton
    fun provideDbPatchRepos(): String = "images"

    @Provides
    fun provideImageSpaceRepo(imageSpaceDao: ImageSpaceDao): ImageSpaceRepo =
        ImageSpaceImpl(imageSpaceDao)

    @Provides
    @Singleton
    fun provideImageSpaceDao(imageSpaceDb: ImageSpaceDb): ImageSpaceDao =
        imageSpaceDb.imageSpaceDao()

    @Provides
    @Singleton
    fun provideImageSpaceDb(context: Context, dbPatch: String): ImageSpaceDb =
        Room.databaseBuilder(context, ImageSpaceDb::class.java, dbPatch).build()
}

@Module
class ModuleRetrofit {

    @Provides
    fun provideSpaceImageApi(retrofit: Retrofit) : SpaceImageApi =
        retrofit.create(SpaceImageApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofitImages(): Retrofit = Retrofit.Builder()
    .baseUrl("https://api.nasa.gov/")
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    .build()
}

@Module
class ModuleCicerone {

    @Provides
    @Singleton
    fun cicerone(): Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.getNavigatorHolder()

    @Provides
    @Singleton
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router

    @Singleton
    @Provides
    fun screens(): IScreens = AndroidScreens()
}

@Singleton
@Component(modules = [ModuleDbImage::class, ModuleRetrofit::class, ModuleCicerone::class])
interface AppComponent {
    fun inject(activity: OpenImage)
    fun inject(activity: Profile)
    fun inject(activity: MainActivity)
}