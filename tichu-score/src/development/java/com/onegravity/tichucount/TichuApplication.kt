package com.onegravity.tichucount

import android.app.Application
import com.facebook.stetho.Stetho

class TichuApplication: TichuApplicationBase() {

    override fun initSDKs(application: Application) {
        Stetho.initializeWithDefaults(application)
    }

    override fun appModule(application: Application) = TichuAppModule(application)

}
