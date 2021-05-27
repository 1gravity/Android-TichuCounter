package com.onegravity.tichucount

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TichuApplication: TichuApplicationBase() {

    override fun initSDKs(application: Application) {
        // nothing to do here
    }

}
