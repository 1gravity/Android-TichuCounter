package com.onegravity.tichucount

import android.app.Application
import toothpick.config.Module
import toothpick.ktp.KTP

const val APP_SCOPE = "APP_SCOPE"

abstract class TichuApplicationBase: Application() {

    override fun onCreate() {
        super.onCreate()

        initSDKs(this)

        KTP.openRootScope()
            .openSubScope(APP_SCOPE)
            .installModules(appModule(this))
            .inject(this)
    }

    abstract fun initSDKs(application: Application)

    abstract fun appModule(application: Application): Module

}
