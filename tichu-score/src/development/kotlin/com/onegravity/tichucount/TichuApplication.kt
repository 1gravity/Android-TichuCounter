package com.onegravity.tichucount

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TichuApplication: TichuApplicationBase() {

    override fun initSDKs(application: Application) {
        Stetho.initializeWithDefaults(application)
    }

}
