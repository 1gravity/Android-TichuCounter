package com.onegravity.tichucount

import android.app.Application

abstract class TichuApplicationBase: Application() {

    override fun onCreate() {
        super.onCreate()

        initSDKs(this)
    }

    abstract fun initSDKs(application: Application)

}
