package com.onegravity.tichucount

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.onegravity.tichucount.db.MatchRepository
import com.onegravity.tichucount.db.TichuDatabase
import com.onegravity.tichucount.util.Logger
import com.onegravity.tichucount.util.LoggerImpl
import com.onegravity.tichucount.viewmodel.MatchViewModel
import com.onegravity.tichucount.viewmodel.MatchesViewModel
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
        bind(MatchRepository::class.java).to(MatchRepository::class.java).singleton()
        bind(MatchesViewModel::class.java).to(MatchesViewModel::class.java).singleton()
        bind(MatchViewModel::class.java).to(MatchViewModel::class.java).singleton()
    }

}
