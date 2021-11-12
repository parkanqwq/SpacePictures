package com.kalabukhov.app.spacepictures

import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.kalabukhov.app.spacepictures.ui.profile.Profile

class AndroidScreens : IScreens {
    override fun users() = ActivityScreen {
        context -> Profile.profileScreen(context)
    }
}
