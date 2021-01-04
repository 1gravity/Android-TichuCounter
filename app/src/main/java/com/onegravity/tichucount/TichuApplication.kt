package com.onegravity.tichucount

import android.app.Application
import com.facebook.stetho.Stetho
import toothpick.ktp.KTP

const val APP_SCOPE = "APP_SCOPE"

class TichuApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        KTP.openRootScope()
            .openSubScope(APP_SCOPE)
            .installModules(TichuAppModule(this))
            .inject(this)
    }

}
