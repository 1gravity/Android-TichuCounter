package com.onegravity.tichucount

import android.app.Application

class TichuApplication: TichuApplicationBase() {

    override fun initSDKs(application: Application) {
        // nothing tp do here
    }

    override fun appModule(application: Application) = TichuAppModule(application)

}