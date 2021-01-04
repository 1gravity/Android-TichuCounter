package com.onegravity.tichucount

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.TichuDatabase
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerImpl
import toothpick.config.Module

class TichuAppModule(application: Application) : Module() {

    init {
        val context = application.applicationContext

        val tichuDB = Room.databaseBuilder(
            context,
            TichuDatabase::class.java, "tichu-db"
        ).build()

        bind(Context::class.java).toInstance(context)
        bind(Logger::class.java).toInstance(LoggerImpl)
        bind(TichuDatabase::class.java).toInstance(tichuDB)
    }

}
