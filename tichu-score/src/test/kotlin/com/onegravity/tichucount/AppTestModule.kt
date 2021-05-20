package com.onegravity.tichucount

import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerTestImpl
import toothpick.config.Module

object AppTestModule : Module() {

    init {
        bind(Logger::class.java).toInstance(LoggerTestImpl)
    }

}
