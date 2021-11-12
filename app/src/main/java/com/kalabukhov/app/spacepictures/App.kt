package com.kalabukhov.app.spacepictures

import android.app.Application
import android.content.Context
import com.kalabukhov.app.spacepictures.di.*

class App : Application() {

    val appComponent : AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .moduleDbImage(ModuleDbImage(this))
            .build()
    }
}

val Context.app: App
    get() = applicationContext as App