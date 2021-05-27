package com.onegravity.tichucount

import android.app.Application
import com.onegravity.tichucount.util.LOGGER_TAG
import com.onegravity.tichucount.util.LoggerImpl
import io.reactivex.rxjava3.plugins.RxJavaPlugins

abstract class TichuApplicationBase: Application() {

    override fun onCreate() {
        super.onCreate()

        // see https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
        RxJavaPlugins.setErrorHandler { e ->
            LoggerImpl(true).e(LOGGER_TAG, "Uncaught rx exception: ${e.cause}", e)
        }

        initSDKs(this)
    }

    abstract fun initSDKs(application: Application)

}
