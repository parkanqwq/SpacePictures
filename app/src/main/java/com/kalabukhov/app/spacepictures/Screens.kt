package com.kalabukhov.app.spacepictures

import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.kalabukhov.app.spacepictures.ui.profile.Profile

object Screens {
    fun Main() = ActivityScreen {
            context -> Profile.profileScreen(context)
    }
}