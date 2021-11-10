package com.kalabukhov.app.spacepictures

import android.app.Application
import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.kalabukhov.app.spacepictures.rest.SpaceImageApi
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    private val retrofit =  Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    val spaceImageApi: SpaceImageApi = retrofit.create(SpaceImageApi::class.java)

    override fun onCreate() {
        super.onCreate()
    }
}

val Context.app: App
    get() = applicationContext as App