package com.onegravity.tichucount

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.onegravity.tichucount.data.MatchRepository
import com.onegravity.tichucount.data.TichuDatabase
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerImpl
import toothpick.config.Module
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
