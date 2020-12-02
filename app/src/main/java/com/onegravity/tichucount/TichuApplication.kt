package com.onegravity.tichucount

import android.app.Application
import android.content.Context
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerImpl
import toothpick.config.Module
import toothpick.ktp.KTP

const val APP_SCOPE = "APP_SCOPE"

class TichuApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        KTP.openRootScope()
            .openSubScope(APP_SCOPE)
            .installModules(AppModule(this))
            .inject(this)
    }

}

class AppModule(application: Application) : Module() {

    init {
        val context = application.applicationContext
        bind(Application::class.java).toInstance(application)
        bind(Context::class.java).toInstance(context)
        bind(Logger::class.java).toInstance(LoggerImpl)
    }

}



